# CLAUDE.md — Development Rules for Claude Code

This file defines the mandatory conventions and architectural rules for all code generation in this project.
Always follow these rules unless the spec explicitly overrides a specific point.

---

## Common Commands

All commands must be run from the project root directory.

| Command          | Description                                              |
|------------------|----------------------------------------------------------|
| `task env-up`    | Start local infrastructure (MongoDB, Kafka, etc.)        |
| `task env-down`  | Stop local infrastructure                                |
| `task start`     | Run the application (requires `task env-up` first)       |
| `task compile`   | Compile the project                                      |
| `task test`      | Run the test suite                                       |

---

## Tech Stack

| Component     | Technology                                          | Version      |
|---------------|-----------------------------------------------------|--------------|
| Language      | Java                                                | 25           |
| Build         | Gradle (Kotlin DSL)                                 | 9.x          |
| Framework     | Spring Boot                                         | 3.5.6        |
| Persistence   | Spring Data MongoDB                                 | Boot-managed |
| Messaging     | Apache Kafka + Spring Kafka                         | 3.3.10       |
| Event schemas | Apache Avro (Confluent)                             | 1.12.0       |
| REST API      | OpenAPI Generator (API-first, custom Gradle plugin) | —            |
| Mapping       | MapStruct                                           | 1.6.3        |
| Boilerplate   | Lombok                                              | Boot-managed |
| Utilities     | Apache Commons Lang 3                               | 3.19.0       |
| JWT           | auth0 Java JWT                                      | 4.5.0        |
| Testing       | JUnit 5 + ApprovalTests + ArchUnit + PIT            | —            |

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

**Rules:**
- One module per Aggregate Root. Never mix aggregates in the same module.
- Shared code between bounded contexts lives in the top-level `shared/` package.
- Shared code within a bounded context lives in `<bounded_context>/shared/`.

---

## Layer Rules (details in skills)

Each layer has a dedicated skill with full rules and code examples. Load the relevant skill when working on that layer:

| Layer          | Skill                 | When to use                                                                        |
|----------------|-----------------------|------------------------------------------------------------------------------------|
| Domain         | `/ddd-domain`         | Aggregates, entities, value objects, identifiers, events, exceptions, repositories |
| Application    | `/ddd-application`    | Use cases, params, idempotency, bulk operations                                    |
| Infrastructure | `/ddd-infrastructure` | REST controllers, Kafka consumers/producers, MongoDB repos, DAOs, mappers          |
| Testing        | `/ddd-testing`        | Use case tests, controller tests, Object Mothers, Fake repositories, ArchUnit      |

---

## Naming Conventions

| Element                   | Convention                                       | Example                                             |
|---------------------------|--------------------------------------------------|-----------------------------------------------------|
| Aggregate factory method  | Descriptive verb (`create`, `createOf`)          | `Order.create(...)`, `Review.createOf(...)`         |
| Identifier factory method | `createOf(parts...)`                             | `OrderLineId.createOf(orderId, productId)`          |
| Action method             | Verb describing the business action              | `update()`, `publish()`, `delete()`                 |
| Ensure method             | `ensure` + invariant description                 | `ensureVersion()`, `ensureOwnershipBelongsToUser()` |
| Repository find single    | `findById` / `findByIdOrError`                   | —                                                   |
| Repository find multiple  | `findAll(criteria)` / `findAllSummary(criteria)` | —                                                   |
| Repository count          | `count(criteria)`                                | —                                                   |
| Use case class            | `<Action>UseCase`                                | `CreateOrderUseCase`                                |
| Params class              | `<Action>Params`                                 | `CreateOrderParams`                                 |
| Event class               | Past tense                                       | `OrderCreated`, `OrderDeleted`                      |
| Exception class           | Entity + problem                                 | `OrderNotFoundException`                            |
| Kafka producer            | Entity + `EventKafkaProducer`                    | `OrderEventKafkaProducer`                           |
| Kafka consumer            | Entity + source + `KafkaConsumer`                | `OrdersOnInventoryEventsKafkaConsumer`              |
| REST DTO suffix           | `RestDTO` (auto-generated)                       | `OrderRestDTO`                                      |
| Mongo DTO suffix          | `MongoDTO`                                       | `OrderMongoDTO`                                     |
| REST Mapper suffix        | `RestMapper`                                     | `OrderRestMapper`                                   |
| Mongo Mapper suffix       | `MongoMapper`                                    | `OrderMongoMapper`                                  |
| Kafka Mapper suffix       | `KafkaMapper`                                    | `OrderKafkaMapper`                                  |
| Mother class              | Entity + `Mother`                                | `OrderMother`                                       |
| Fake repository           | Entity + `FakeRepository`                        | `OrderFakeRepository`                               |
| Fake service              | Entity + `Fake` + PortName                       | `ExternalFakeFinder`                                |

**Variable naming:** always use descriptive names. Prefer `var` for declarations.

---

## What NOT to Generate

- No setters on aggregates, entities, or value objects.
- No repository interfaces for plain Entities (only for Aggregate Roots).
- No business logic inside infrastructure adapters or controllers.
- No framework imports inside `domain/`.
- No generic variable names (`data`, `obj`, `temp`).
- No comments for code that should be self-explanatory — rename instead.
- No handwritten REST DTOs — they are generated from OpenAPI specs.
- No handwritten Avro DTOs — they are generated from `.avsc` schemas.
