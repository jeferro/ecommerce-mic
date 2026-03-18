---
name: ddd-domain
description: Rules and patterns for the Domain layer in DDD+Hexagonal architecture. Use when creating or modifying domain models (aggregates, entities, value objects, identifiers), domain events, domain exceptions, or repository interfaces.
---

# Domain Layer — Rules & Patterns

## General Rules

- **No framework dependencies** in `domain/`. Only Lombok, Apache Commons, and lib-shared DDD classes are allowed.
- Never use setters on aggregates, entities, or value objects. All state changes go through action methods.
- All action methods and factory methods must validate their inputs using `ValueValidator`.
- Methods that check an invariant and throw a domain exception must be named with the `ensure` prefix (e.g. `ensureOwnershipBelongsToUser()`).
- Prefer `var` for local variable declarations.
- Write semantic, self-explanatory code. Avoid comments except to document deliberate technical decisions.

---

## Identifiers

- Every Aggregate Root, Entity, and Projection must have an `Id` class extending `StringIdentifier`.
- Identifiers live in `domain/models/`.
- Identifiers can be **rich objects** — they may carry parsed domain data (e.g. a composite ID holds multiple fields embedded in a string `"part1::part2"`).
- Always provide a public constructor that parses the raw string value, and a static `createOf(...)` factory method for construction with validation.
- Use `::` as separator when composing multi-part string identifiers.

```java
@Getter
public class OrderLineId extends StringIdentifier {

    private static final String SEPARATOR = "::";

    private final OrderId orderId;
    private final ProductId productId;

    public OrderLineId(String value) {
        super(value);
        var split = value.split(SEPARATOR);
        if (split.length != 2) {
            throw ValueValidationException.createOfMessage("Incorrect format " + value);
        }
        this.orderId = new OrderId(split[0]);
        this.productId = new ProductId(split[1]);
    }

    private OrderLineId(OrderId orderId, ProductId productId) {
        super(orderId + SEPARATOR + productId);
        this.orderId = orderId;
        this.productId = productId;
    }

    public static OrderLineId createOf(OrderId orderId, ProductId productId) {
        ValueValidator.ensureNotNull(orderId, "orderId");
        ValueValidator.ensureNotNull(productId, "productId");
        return new OrderLineId(orderId, productId);
    }
}
```

---

## Value Objects

- Extend `ValueObject`.
- Validations go in the constructor.
- Throw `ValueValidationException` for format/constraint violations.

---

## Aggregates & Entities

- Always create business instances via a **named static factory method** (e.g. `create(...)`, `createOf(...)`). Never expose constructors for business creation.
- Constructors are public and used only inside Mothers, Mappers, and the aggregate's own factory methods.
- Annotate with `@Getter` (Lombok). Never add public setters.
- Each use case corresponds to one **public action method** on the aggregate (e.g. `update()`, `publish()`, `delete()`).
- Action methods call `ValueValidator` for parameter validation, then `record(event)` for domain events.
- Optimistic locking: mutating action methods call `ensureVersion(version)` to prevent concurrent modification.
- Business invariants enforced via methods named `ensure*()` that throw domain exceptions (may be public if called from use cases).
- Encapsulate all calculations inside the aggregate or entity (e.g. `calculateTotalPrice()`).

```java
@Getter
public class Order extends AggregateRoot<OrderId> {

    protected OrderStatus status;
    // ... other fields

    public Order(OrderId id, /* all fields */, long version, Metadata metadata) {
        super(id, version, metadata);
        // assign fields
    }

    public static Order create(OrderId orderId, /* params */) {
        ValueValidator.ensureNotNull(orderId, "orderId");
        // ... more validations
        var order = new Order(orderId, /* ... */, 0L, null);
        order.record(OrderCreated.create(order));
        return order;
    }

    public void update(/* params */, long version) {
        ValueValidator.ensureNotNull(/* param */, "param");
        // ... more validations
        ensureVersion(version);
        // mutate fields
        record(OrderUpdated.create(this));
    }

    public void ensureOwnershipBelongsToUser(Auth auth) {
        // throw ForbiddenException if not owner
    }
}
```

---

## Summaries

- A Summary is a lightweight read-only projection of an aggregate for list responses.
- Extends `AggregateRoot<Id>` directly (same base class as the aggregate, not a separate `View` class).
- Lives in the same `domain/models/` package as the aggregate.
- Override `getId()` with `@Deprecated` and provide a semantically named alternative (e.g. `getOrderId()`).
- Returned by the repository via a dedicated `findAllSummary(criteria)` method.

---

## Projections

- Extend `Projection<Id>` from lib-shared.
- Represent an aggregate from an external Bounded Context.
- May contain read-oriented business logic specific to the current BC.
- Accessed via a domain service interface (Finder/port). Implementation lives in infrastructure.

---

## Events

- Extend the aggregate's base event class (e.g. `OrderEvent` extends `Event<Order, OrderId>`).
- Class names use **past tense** (e.g. `OrderCreated`, `OrderDeleted`).
- Provide a static `create(Aggregate)` factory method that takes the full aggregate snapshot.
- `Event` from lib-shared holds the full entity snapshot, an `EventId`, and `sentAt`.

---

## Exceptions

- Extend the appropriate base: `NotFoundException`, `ForbiddenException`, `ValueValidationException`, or `IncorrectVersionException`.
- Always provide a static `createOf(...)` factory method (additional named factory methods are allowed when semantics require it).
- Constructor takes `(code, title, message)`. The title is always a fixed string; code and message are parameterized.
- Error code constants are defined in `shared/domain/exceptions/<Context>ExceptionCodes.java`.

```java
public class OrderNotFoundException extends NotFoundException {
    public static OrderNotFoundException createOf(OrderId id) {
        return new OrderNotFoundException(ORDER_NOT_FOUND, "Order not found",
                "Order " + id + " not found");
    }
}
```

---

## Repositories

- One interface per Aggregate Root. Never create a repository for a plain Entity.
- Method naming:
  - `save(entity)` — create or update, returns the saved entity.
  - `findById(id)` — returns `Optional`.
  - `findByIdOrError(id)` — default method, throws `NotFoundException`.
  - `findOne(criteria)` — default method returning `Optional`, built on `findAll`.
  - `findAll(criteria)` — returns `List`.
  - `findAllSummary(criteria)` — returns `List<Summary>`.
  - `count(criteria)` — returns `long`.
  - `delete(entity)`.
- Use the **Criteria pattern** for multi-record queries. Add `default` convenience methods built on top of `findAll`.

```java
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId orderId);
    default Order findByIdOrError(OrderId orderId) {
        return findById(orderId)
                .orElseThrow(() -> OrderNotFoundException.createOf(orderId));
    }
    void delete(Order order);
    List<Order> findAll(OrderCriteria criteria);
    default Optional<Order> findOne(OrderCriteria criteria) { ... }
    long count(OrderCriteria criteria);
    List<OrderSummary> findAllSummary(OrderCriteria criteria);
}
```

---

## Domain Services

- Either an **output port interface** (implemented in infrastructure, e.g. `PasswordEncoder`, `ExternalFinder`) or a class with **business logic not belonging to any single aggregate** (e.g. `OrderValidator`).
- Interface names describe the capability, not the technology.
