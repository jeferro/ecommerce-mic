# AGENTS.md — Development Rules for Claude Code

This file defines the mandatory conventions and architectural rules for all code generation in this project.
Always follow these rules unless the spec explicitly overrides a specific point.

---

## Architecture: DDD + Hexagonal (Ports & Adapters)

### Package Structure

The root structure reflects **Bounded Contexts** and their **Aggregate Roots**:

```
src/
  <bounded_context>/
    <aggregate_root>/
      application/
        params/
      domain/
        models/
        repositories/
        services/
        exceptions/
        events/
      infrastructure/
        <technology>/        # e.g. rest_api, mongo, postgres, kafka
          config/            # (optional)
          dtos/              # (optional)
          daos/              # (optional)
          mappers/
          services/          # (optional)
    shared/
      domain/
      infrastructure/
  shared/
    domain/
```

**Rules:**
- One module per Aggregate Root. Never mix aggregates in the same module.
- `views/` is a sibling of the aggregate module when read-models exist.
- Shared code between bounded contexts lives in the top-level `shared/` package.
- Shared code within a bounded context lives in `<bounded_context>/shared/`.

---

## Domain Layer

### General Rules
- **No framework dependencies** in `domain/`. Only Lombok and Apache Commons are allowed.
- Never use setters on aggregates, entities, or value objects. All state changes go through action methods.
- All action methods and factory methods must validate their inputs.
- Validation methods that throw a domain exception must be named with the `ensure` prefix (e.g. `ensureInvoiceIsNotPaid()`).
- Prefer `var` for local variable declarations unless the compiler requires an explicit type.
- Write semantic, self-explanatory code. Avoid comments except to clarify external API behavior or document a deliberate technical decision that prevents a known bug.

### Identifiers
- Every Aggregate Root, Entity, and Projection must have an `Id` class extending `UUIDIdentifier` or `StringIdentifier`.
- Identifiers live in `domain/models/`.
- Always provide a static `createOf()` factory method that generates the value.

```java
public class InvoiceId extends UUIDIdentifier {
    public InvoiceId(String value) { super(value); }
    public static InvoiceId createOf() { return new InvoiceId(generateUuid()); }
}
```

### Value Objects
- Extend `ValueObject`.
- Validations go in the constructor (not a factory method), because they are used as use-case params.
- Throw `ValueValidationException` for format/constraint violations.

### Aggregates & Entities
- Always create instances via a **named static factory method** (e.g. `createEmpty()`, `createOf()`). Never expose constructors for business creation.
- Use constructors only inside Mothers and Mappers.
- Each use case corresponds to one **public action method** on the aggregate (e.g. `addLine()`, `paid()`).
- Action methods must record domain events via `record(event)`.
- Business invariants are enforced by private `ensure*()` methods that throw domain exceptions.
- Encapsulate all calculations inside the aggregate or entity (e.g. `getTotal()`, `belongsTo()`).
- **Project attributes from other aggregates** when needed for: business logic, filtering, list summaries, always-displayed data, performance, or saving temporal state.

```java
@AllArgsConstructor
public class Invoice extends AggregateRoot<InvoiceId> {
    // Fields, no public setters

    public static Invoice createEmpty(LocalDate date) { ... }

    public void addLine(Product product, long amount) {
        ensureInvoiceIsNotPaid();
        // validation + logic + record(event)
    }

    private void ensureInvoiceIsNotPaid() {
        if (paid) throw InvoiceAlreadyPaidException.createOf(id);
    }
}
```

### Projections
- Extend `Projection<Id>`.
- Represent an aggregate from an external Bounded Context.
- May contain read-oriented business logic specific to the current BC.
- Primary operation is query (or update if persisted locally for resilience).

### Views & Summaries
- Extend `View<Id>`. Read-only, no repository.
- Accessed via a `Finder` interface (query methods only, no mutations).
- `Summary` views live in the same module as their aggregate — no separate module needed.
- Composition (REST or DB) is implemented in the infrastructure layer; complex composition logic may live in domain.

### Events
- Extend the aggregate's base event class (e.g. `InvoiceEvent`).
- Class names use **past tense** (e.g. `InvoiceLineAdded`, `InvoiceCreated`).
- Provide a static `create(...)` factory method.

### Exceptions
- Extend the appropriate base: `NotFoundException`, `ForbiddenException`, `ValueValidationException`, or `IncorrectVersionException`.
- Always provide a static `createOf(...)` factory method.
- The exception title is always fixed; only the error code and message are parameterized.

```java
public class InvoiceNotFoundException extends NotFoundException {
    public static InvoiceNotFoundException createOf(InvoiceId id) {
        return new InvoiceNotFoundException(INVOICE_NOT_FOUND, "Invoice " + id + " not found");
    }
}
```

### Repositories
- One interface per Aggregate Root. Never create a repository for a plain Entity.
- Method naming: `save()` (create or update, returns the saved entity), `findById()` (returns `Optional`), `findByIdOrError()` (default method, throws `NotFoundException`), `delete()`, `deleteAll()`, `findAllByCriteria()`, `countByCriteria()`.
- Use the **Criteria pattern** for multi-record queries to minimize the number of repository methods.
- Add `default` methods to the interface for common query combinations.

```java
public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(InvoiceId id);
    default Invoice findByIdOrError(InvoiceId id) { ... }
    void delete(Invoice invoice);
    List<InvoiceSummary> findAllByCriteria(InvoiceCriteria criteria);
    long countByCriteria(InvoiceCriteria criteria);
}
```

### Domain Services
- Either an **output port interface** (implemented in infrastructure, e.g. `EmailNotifier`) or a class with **business logic not belonging to any single aggregate**.
- Interfaces follow the same pattern as repositories: name describes the capability, not the technology.

---

## Application Layer

### Params
- One `Params` class per use case, extending `Params<ReturnType>`.
- Constructor validates **required fields only** using `ValueValidator.isRequired(...)`. All other validations are domain rules.
- Attributes must be **primitive types or Value Objects**. Never pass aggregates or entities.
- Use an `Input` suffix class (e.g. `InvoiceLineInput`) when grouping related value objects for a param.

### Use Cases
- One class per use case, named `<Action>UseCase` (describes behavior, e.g. `AddInvoiceLineUseCase`).
- Extend `UseCase<Params, ReturnType>` and annotate with `@Component`.
- Declare `getMandatoryUserRoles()` for authorization.
- Responsibilities: manage transactionality, validate referenced entities exist, call aggregate action methods, persist via repository, publish events via `EventBus`.
- A use case should ideally call **one action method on one aggregate**. Synchronous multi-aggregate operations are the exception.
- In complex logic, the `execute()` method should read as a list of steps, each delegating to a private method.

```java
@Component
@RequiredArgsConstructor
public class AddInvoiceLineUseCase extends UseCase<AddInvoiceLineParams, Invoice> {
    @Override
    public Invoice execute(Auth auth, AddInvoiceLineParams params) {
        var product = productsRepository.findByIdOrError(params.getProductId());
        var invoice = invoicesRepository.findByIdOrError(params.getInvoiceId());
        invoice.addLine(product, params.getAmount());
        invoicesRepository.save(invoice);
        eventBus.sendAll(invoice);
        return invoice;
    }
}
```

### Idempotency
- Synchronous use cases: use an aggregate version code to prevent concurrent modification.
- Asynchronous use cases: store a `lastUpdatedAt` timestamp and skip the operation if the incoming date is older.
- Apply only when it makes sense for the specific use case.

### Bulk Operations
- Split work into pages, process pages in **parallel** (`IntStream.range(...).parallel()`).
- Avoid global transactions; use per-batch transactionality.
- Log errors per batch without blocking the rest (`try/catch` per batch).

### Use Case Testing
- Use **solidarity testing** (sociable tests): test use cases as a black box.
- Repositories → use the **Fake** in-memory implementation.
- Domain services (domain implementation) → use the **real class**.
- Domain services (infrastructure implementation) → use a **Mock** class.
- Do not mock aggregates or entities.
- Value Objects → unit-test independently.

---

## Infrastructure Layer

### Package Naming
Name packages after the technology they integrate with:
`rest_api`, `rest_api_v1`, `grpc_api`, `rest_client`, `mongo`, `postgres`, `kafka`

### Primary Adapters (Inbound)
- Responsible for data conversion and delegating to use cases via `UseCaseBus`.
- No business logic inside controllers or consumers.

### Secondary Adapters (Outbound)
- Implement domain repository/service interfaces.
- Responsible for DTO↔Domain conversion (via Mappers) and persistence/external calls.

### Mappers
- One Mapper class **per aggregate** (not per class), grouping all conversion methods.
- Extract reusable mappers (e.g. `AddressRestMapper`) only when shared across multiple aggregate mappers.

### DTOs
- May be auto-generated (OpenAPI, Avro, Protobuf). Do not hand-write if a generator is configured.

---

## Testing — Mothers & Fakes

### Object Mothers
- One `Mother` class per aggregate/entity, located in the test source tree.
- Use **constructors directly** (not factory methods) to avoid triggering domain events.
- Provide named methods that describe the state (e.g. `createUnpaidEmptyInvoice()`, `createPaidInvoice()`).

### In-Memory Fakes
- Implement the repository interface using in-memory maps.
- Pre-populate with Mother objects in the constructor.
- Implement all query methods with in-memory filtering and sorting.

---

## Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Factory method | Descriptive verb phrase | `createEmpty()`, `createOf()` |
| Action method | Verb describing the business action | `addLine()`, `paid()` |
| Ensure method | `ensure` + invariant description | `ensureInvoiceIsNotPaid()` |
| Repository find single | `findById` / `findByIdOrError` | — |
| Repository find multiple | `findAllByCriteria` | — |
| Use case class | `<Action>UseCase` | `DeleteAllInvoicesUseCase` |
| Event class | Past tense | `InvoiceLineAdded` |
| Exception class | Entity + problem | `InvoiceNotFoundException` |
| DTO suffix | By layer — `RestDTO`, `MongoDTO` | `InvoiceRestDTO` |
| Mapper suffix | By layer — `RestMapper`, `MongoMapper` | `InvoiceRestMapper` |

**Variable naming:** always use descriptive names. Prefer `var` for declarations.
**Method naming:** prefer explicit over generic (`findUserOrNull` over `getUser`, `ensurePermissionsOfUser` over `validateUser`).

---

## What NOT to Generate

- No setters on aggregates, entities, or value objects.
- No repository interfaces for plain Entities (only for Aggregate Roots).
- No business logic inside infrastructure adapters or controllers.
- No framework imports inside `domain/`.
- No generic variable names (`data`, `obj`, `temp`).
- No comments for code that should be self-explanatory — rename instead.
