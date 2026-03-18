---
name: ddd-testing
description: Rules and patterns for testing in DDD+Hexagonal architecture. Use when creating or modifying use case tests, REST controller tests, Object Mothers, in-memory Fake repositories, or ArchUnit tests.
---

# Testing — Rules & Patterns

## Use Case Tests (Solidarity / Sociable)

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

---

## REST Controller Tests

- `@WebMvcTest` + `@Import` for mapper implementation.
- Use `StubUseCaseBus` instead of the real bus — records params, returns a preset result.
- Use `ApprovalUtils.verifyAll(params, status, responseBody)` for snapshot-based verification.
- Extend `RestControllerTest` base which provides JWT token constants and security config.

---

## Object Mothers

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

---

## In-Memory Fakes

- Extend `FakeRepository<Aggregate, Id>` from the test shared module and implement the domain repository interface.
- Pre-populate with Mother objects in the constructor via `save()`.
- Implement `findAll(criteria)` with in-memory filtering and sorting.
- Implement `findAllSummary(criteria)` by mapping from `findAll(criteria)`.
- Implement `count(criteria)` as `findAll(criteria).size()`.

---

## ArchUnit Tests

- Enforce that `domain/` cannot depend on `application/` or `infrastructure/`.
- Enforce that `application/` cannot depend on `infrastructure/`.
- Enforce that exceptions live in the `exceptions` package and params in the `params` package.
- No `services` layer is allowed inside `application/`.
