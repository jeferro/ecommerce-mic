---
name: ddd-infrastructure
description: Rules and patterns for the Infrastructure layer in DDD+Hexagonal architecture. Use when creating or modifying REST controllers, Kafka consumers/producers, MongoDB repositories, DAOs, mappers, or DTOs.
---

# Infrastructure Layer — Rules & Patterns

## Package Naming

Name packages after the technology and version:
`rest_api/v1`, `mongo`, `kafka/v1`, `bcrypt`, `mock`

---

## Primary Adapters — REST Controllers

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

---

## Primary Adapters — Kafka Consumers

- Annotate with `@KafkaListener` per topic.
- Use `@KafkaHandler` per Avro type and a `@KafkaHandler(isDefault = true)` to silently ignore unknown types.
- Call `useCaseBus.executeWithRetry()` for idempotent processing.

---

## Secondary Adapters — Repositories

- Annotate with `@Component @RequiredArgsConstructor`, implement the domain repository interface.
- Delegate all work to a `MongoDao` + `Mapper`. No business logic.

---

## Secondary Adapters — Event Producers

- Implement `EventBusProducer<EventBaseClass>`.
- Automatically registered by `SpringEventBus` (scans for all `EventBusProducer` beans).
- Use sealed-class `switch` expression in the Kafka mapper to dispatch to specific Avro DTOs.

---

## MongoDao

- Extend `MongoDao<DTO, ID, Criteria>` from lib-shared.
- Override `mapCriteria()` to translate the domain Criteria object to Spring Data `Criteria`.
- Override `mapOrder()` to map the order enum to the MongoDB field name.

---

## Mappers

- Annotate with `@Mapper(config = MapstructConfig.class)`, extend the appropriate base:
  - `AggregatePrimaryMapper` — domain → REST DTO (outbound from domain).
  - `AggregateSecondaryMapper` — Mongo/Kafka DTO → domain (inbound to domain).
  - `EventMapper` — domain Event → Avro DTO.
- One Mapper class **per aggregate** (not per class), grouping all conversion methods.
- DTOs for REST are auto-generated from OpenAPI specs. Do not hand-write them.
- DTOs for Kafka are auto-generated from Avro `.avsc` schemas. Do not hand-write them.
- MongoDB DTOs extend `AuditedMongoDTO` (adds `version` + `MetadataMongoDTO`). Annotate with `@Document`.
