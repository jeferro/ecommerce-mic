---
name: ddd-application
description: Rules and patterns for the Application layer in DDD+Hexagonal architecture. Use when creating or modifying use cases, params classes, or handling idempotency and bulk operations.
---

# Application Layer — Rules & Patterns

## General Rules

- The application layer can only depend on `domain/`. Never import from `infrastructure/`.
- No business logic here — delegate all decisions to domain aggregates.

---

## Params

- One `Params` class per use case, extending `Params<ReturnType>`.
- Constructor assigns all fields without validation — validation is the aggregate's responsibility.
- Attributes must be **primitive types, Value Objects, or Identifiers**. Never pass aggregates or entities.
- Annotate with `@Getter` (Lombok).

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

---

## Use Cases

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

---

## Idempotency

- Synchronous use cases: pass the aggregate `version` through params and call `ensureVersion(version)` inside the aggregate action method.
- Asynchronous Kafka use cases: use `useCaseBus.executeWithRetry()` (3 retries) for resilience.
- Apply only when it makes sense for the specific use case.

---

## Bulk Operations

- Split work into pages, process pages in **parallel** (`IntStream.range(...).parallel()`).
- Avoid global transactions; use per-batch transactionality.
- Log errors per batch without blocking the rest (`try/catch` per batch).
