# CLAUDE.md — Development Rules for Claude Code

This file defines the mandatory conventions and architectural rules for all code generation in this project.
Always follow these rules unless the spec explicitly overrides a specific point.

---

## Tech Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 25 |
| Build | Gradle (Kotlin DSL) | 9.x |
| Framework | Spring Boot | 3.5.6 |
| Persistence | Spring Data MongoDB | Boot-managed |
| Messaging | Apache Kafka + Spring Kafka | 3.3.10 |
| Event schemas | Apache Avro (Confluent) | 1.12.0 |
| REST API | OpenAPI Generator (API-first, custom Gradle plugin) | — |
| Mapping | MapStruct | 1.6.3 |
| Boilerplate | Lombok | Boot-managed |
| Utilities | Apache Commons Lang 3 | 3.19.0 |
| JWT | auth0 Java JWT | 4.5.0 |
| Testing | JUnit 5 + ApprovalTests + ArchUnit + PIT | — |

---

## Architecture: DDD + Hexagonal (Ports & Adapters)

### Project Layout

```
code/
  lib-shared/               # Shared DDD framework (base classes, infra config)
  app/                      # Application module (all bounded contexts)
    src/main/java/<base_package>/
      <bounded_context>/
        <aggregate_root>/
          application/
            params/
          domain/
            models/
            models/criteria/
            repositories/
            services/
            exceptions/
            events/
          infrastructure/
            <technology>/   # e.g. rest_api/v1, mongo, kafka/v1
              config/       # (optional)
              dtos/         # (optional)
              daos/         # (optional)
              mappers/
        shared/
          domain/
          infrastructure/
      shared/
        domain/
apis/
  rest/v1/                  # OpenAPI YAML specs (source of truth for REST DTOs)
tools/
  docker/docker-compose.yml # Local infra (MongoDB, Kafka)
```

### Modules

**Rules:**
- One module per Aggregate Root. Never mix aggregates in the same module.
- Shared code between bounded contexts lives in the top-level `shared/` package.
- Shared code within a bounded context lives in `<bounded_context>/shared/`.

---

## Domain Layer

### General Rules
- **No framework dependencies** in `domain/`. Only Lombok, Apache Commons, and lib-shared DDD classes are allowed.
- Never use setters on aggregates, entities, or value objects. All state changes go through action methods.
- All action methods and factory methods must validate their inputs using `ValueValidator`.
- Methods that check an invariant and throw a domain exception must be named with the `ensure` prefix (e.g. `ensureOwnershipBelongsToUser()`).
- Prefer `var` for local variable declarations.
- Write semantic, self-explanatory code. Avoid comments except to document deliberate technical decisions.

### Identifiers
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

### Value Objects
- Extend `ValueObject`.
- Validations go in the constructor.
- Throw `ValueValidationException` for format/constraint violations.

### Aggregates & Entities
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

### Summaries
- A Summary is a lightweight read-only projection of an aggregate for list responses.
- Extends `AggregateRoot<Id>` directly (same base class as the aggregate, not a separate `View` class).
- Lives in the same `domain/models/` package as the aggregate.
- Override `getId()` with `@Deprecated` and provide a semantically named alternative (e.g. `getOrderId()`).
- Returned by the repository via a dedicated `findAllSummary(criteria)` method.

### Projections
- Extend `Projection<Id>` from lib-shared.
- Represent an aggregate from an external Bounded Context.
- May contain read-oriented business logic specific to the current BC.
- Accessed via a domain service interface (Finder/port). Implementation lives in infrastructure.

### Events
- Extend the aggregate's base event class (e.g. `OrderEvent` extends `Event<Order, OrderId>`).
- Class names use **past tense** (e.g. `OrderCreated`, `OrderDeleted`).
- Provide a static `create(Aggregate)` factory method that takes the full aggregate snapshot.
- `Event` from lib-shared holds the full entity snapshot, an `EventId`, and `sentAt`.

### Exceptions
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

### Repositories
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

### Domain Services
- Either an **output port interface** (implemented in infrastructure, e.g. `PasswordEncoder`, `ExternalFinder`) or a class with **business logic not belonging to any single aggregate** (e.g. `OrderValidator`).
- Interface names describe the capability, not the technology.

---

## Application Layer

### Params
- One `Params` class per use case, extending `Params<ReturnType>`.
- Constructor assigns all fields without validation — validation is the aggregate's responsibility.
- Attributes must be **primitive types, Value Objects, or Identifiers**. Never pass aggregates or entities.
- Annotate with `@Getter` (Lombok).
- Application layer only can depends on 'domain/'

```java
@Getter
public class CreateOrderParams extends Params<Order> {
    private final OrderId orderId;
    private final CustomerId customerId;
    // ... other fields

    public CreateOrderParams(OrderId orderId, CustomerId customerId /* ... */) {
        super();
        this.orderId = orderId;
        this.customerId = customerId;
        // ... assign fields
    }
}
```

### Use Cases
- One class per use case, named `<Action>UseCase` (e.g. `CreateOrderUseCase`).
- Extend `UseCase<Params, ReturnType>` and annotate with `@Component @RequiredArgsConstructor`.
- Declare `getMandatoryUserRoles()` returning a `Set<String>` of role constants from `Roles`.
- The `execute()` method should read as a sequential list of named steps, each delegating to a private method.
- A use case should ideally call **one action method on one aggregate**. Multi-aggregate operations within a single use case are an intentional exception.
- Event ordering: call `eventBus.sendAll(aggregate)` **before** `repository.save()` for new aggregates; after `save()` for updates to existing aggregates.

```java
@Component
@RequiredArgsConstructor
public class CreateOrderUseCase extends UseCase<CreateOrderParams, Order> {

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Order execute(Auth auth, CreateOrderParams params) {
        var orderId = params.getOrderId();

        ensureOrderNotExist(orderId);
        validateExternalDependencies(params);
        return createOrder(orderId, params);
    }

    private void ensureOrderNotExist(OrderId orderId) { ... }
    private void validateExternalDependencies(CreateOrderParams params) { ... }
    private Order createOrder(OrderId orderId, CreateOrderParams params) { ... }
}
```

### Idempotency
- Synchronous use cases: pass the aggregate `version` through params and call `ensureVersion(version)` inside the aggregate action method.
- Asynchronous Kafka use cases: use `useCaseBus.executeWithRetry()` (3 retries) for resilience.
- Apply only when it makes sense for the specific use case.

### Bulk Operations
- Split work into pages, process pages in **parallel** (`IntStream.range(...).parallel()`).
- Avoid global transactions; use per-batch transactionality.
- Log errors per batch without blocking the rest (`try/catch` per batch).

---

## Infrastructure Layer

### Package Naming
Name packages after the technology and version:
`rest_api/v1`, `mongo`, `kafka/v1`, `bcrypt`, `mock`

### Primary Adapters — REST Controllers
- Implement the interface generated from the OpenAPI spec (API-first).
- Annotate with `@RestController @RequiredArgsConstructor`.
- Delegate data conversion to the `RestMapper`, then dispatch to `useCaseBus.execute(params)`.
- No business logic inside controllers.

```java
@RestController
@RequiredArgsConstructor
public class OrderRestController implements OrdersApi {
    private final OrderRestMapper orderRestMapper;
    private final UseCaseBus useCaseBus;

    @Override
    public OrderRestDTO createOrder(String orderId, CreateOrderInputRestDTO body) {
        var params = orderRestMapper.toCreateOrderParams(
                orderRestMapper.toDomain(orderId), body);
        var order = useCaseBus.execute(params);
        return orderRestMapper.toDTO(order);
    }
}
```

### Primary Adapters — Kafka Consumers
- Annotate with `@KafkaListener` per topic.
- Use `@KafkaHandler` per Avro type and a `@KafkaHandler(isDefault = true)` to silently ignore unknown types.
- Call `useCaseBus.executeWithRetry()` for idempotent processing.

### Secondary Adapters — Repositories
- Annotate with `@Component @RequiredArgsConstructor`, implement the domain repository interface.
- Delegate all work to a `MongoDao` + `Mapper`. No business logic.

### Secondary Adapters — Event Producers
- Implement `EventBusProducer<EventBaseClass>`.
- Automatically registered by `SpringEventBus` (scans for all `EventBusProducer` beans).
- Use sealed-class `switch` expression in the Kafka mapper to dispatch to specific Avro DTOs.

### MongoDao
- Extend `MongoDao<DTO, ID, Criteria>` from lib-shared.
- Override `mapCriteria()` to translate the domain Criteria object to Spring Data `Criteria`.
- Override `mapOrder()` to map the order enum to the MongoDB field name.

### Mappers
- Annotate with `@Mapper(config = MapstructConfig.class)`, extend the appropriate base:
  - `AggregatePrimaryMapper` — domain → REST DTO (outbound from domain).
  - `AggregateSecondaryMapper` — Mongo/Kafka DTO → domain (inbound to domain).
  - `EventMapper` — domain Event → Avro DTO.
- One Mapper class **per aggregate** (not per class), grouping all conversion methods.
- DTOs for REST are auto-generated from OpenAPI specs. Do not hand-write them.
- DTOs for Kafka are auto-generated from Avro `.avsc` schemas. Do not hand-write them.
- MongoDB DTOs extend `AuditedMongoDTO` (adds `version` + `MetadataMongoDTO`). Annotate with `@Document`.

---

## Testing

### Use Case Tests (Solidarity / Sociable)
- Pure JUnit 5, no Spring context.
- Wire collaborators manually in `@BeforeEach`.
- Repositories → use the **Fake** in-memory implementation.
- Domain services (domain implementation) → use the **real class**.
- Domain services (infrastructure implementation) → use a **Fake** or **Mock**.
- Do not mock aggregates or entities.
- Value Objects → unit-test independently.
- Assertions verify: return value, fake repository state, event bus contents.

```java
class CreateOrderUseCaseTest {
    @BeforeEach
    void beforeEach() {
        var eventFakeBus = new EventFakeBus();
        var orderFakeRepository = new OrderFakeRepository();
        var externalFinder = new ExternalFakeFinder();
        var orderValidator = new OrderValidator(externalFinder);
        createOrderUseCase = new CreateOrderUseCase(
                orderFakeRepository, orderValidator, eventFakeBus);
    }
}
```

### REST Controller Tests
- `@WebMvcTest` + `@Import` for mapper implementation.
- Use `StubUseCaseBus` instead of the real bus — records params, returns a preset result.
- Use `ApprovalUtils.verifyAll(params, status, responseBody)` for snapshot-based verification.
- Extend `RestControllerTest` base which provides JWT token constants and security config.

### Object Mothers
- One `Mother` class per aggregate/entity, in the test source tree, same package as the domain class.
- Use **constructors directly** (not factory methods) to create objects in any desired state without triggering domain events.
- Provide named methods that describe the domain state (e.g. `pendingOrder()`, `completedOrder()`, `orderSummary()`).

```java
public class OrderMother {
    public static Order pendingOrder() {
        var id = OrderId.createOf(/* ... */);
        return new Order(id, /* fields */, PENDING, 1L, null);
    }
}
```

### In-Memory Fakes
- Extend `FakeRepository<Aggregate, Id>` from the test shared module and implement the domain repository interface.
- Pre-populate with Mother objects in the constructor via `save()`.
- Implement `findAll(criteria)` with in-memory filtering and sorting.
- Implement `findAllSummary(criteria)` by mapping from `findAll(criteria)`.
- Implement `count(criteria)` as `findAll(criteria).size()`.

---

## Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Aggregate factory method | Descriptive verb (`create`, `createOf`) | `Order.create(...)`, `Review.createOf(...)` |
| Identifier factory method | `createOf(parts...)` | `OrderLineId.createOf(orderId, productId)` |
| Action method | Verb describing the business action | `update()`, `publish()`, `delete()` |
| Ensure method | `ensure` + invariant description | `ensureVersion()`, `ensureOwnershipBelongsToUser()` |
| Repository find single | `findById` / `findByIdOrError` | — |
| Repository find multiple | `findAll(criteria)` / `findAllSummary(criteria)` | — |
| Repository count | `count(criteria)` | — |
| Use case class | `<Action>UseCase` | `CreateOrderUseCase` |
| Params class | `<Action>Params` | `CreateOrderParams` |
| Event class | Past tense | `OrderCreated`, `OrderDeleted` |
| Exception class | Entity + problem | `OrderNotFoundException` |
| Kafka producer | Entity + `EventKafkaProducer` | `OrderEventKafkaProducer` |
| Kafka consumer | Entity + source + `KafkaConsumer` | `OrdersOnInventoryEventsKafkaConsumer` |
| REST DTO suffix | `RestDTO` (auto-generated) | `OrderRestDTO` |
| Mongo DTO suffix | `MongoDTO` | `OrderMongoDTO` |
| REST Mapper suffix | `RestMapper` | `OrderRestMapper` |
| Mongo Mapper suffix | `MongoMapper` | `OrderMongoMapper` |
| Kafka Mapper suffix | `KafkaMapper` | `OrderKafkaMapper` |
| Mother class | Entity + `Mother` | `OrderMother` |
| Fake repository | Entity + `FakeRepository` | `OrderFakeRepository` |
| Fake service | Entity + `Fake` + PortName | `ExternalFakeFinder` |

**Variable naming:** always use descriptive names. Prefer `var` for declarations.

---

## What NOT to Generate

- No setters on aggregates, entities, or value objects.
- No repository interfaces for plain Entities (only for Aggregate Roots).
- No business logic inside infrastructure adapters or controllers.
- No framework imports inside `domain/`.
- No generic variable names (`data`, `obj`, `temp`).
- No comments for code that should be self-explanatory — rename instead.
- No hand-written REST DTOs — they are generated from OpenAPI specs.
- No hand-written Avro DTOs — they are generated from `.avsc` schemas.
