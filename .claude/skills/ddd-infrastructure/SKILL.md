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

---

## Bruno API Collection (`tools/bruno`)

Every change to a REST API **must** be reflected in the Bruno collection at `tools/bruno/`.

### Structure

```
tools/bruno/
  bruno.json                   # Collection metadata
  collection.bru               # Root config (auth, shared vars)
  environments/
    local.bru                  # Environment vars: ecommerce-url, jwt-token, etc.
  v1/
    folder.bru                 # Folder: v1
    <resource>/
      folder.bru               # Folder per resource
      <Operation>.bru          # One file per endpoint
```

### Rules

- One `.bru` file per endpoint operation. The file name is the human-readable operation name (e.g. `Create product versions.bru`).
- Files are ordered with `seq` in the `meta` block. Use sequential numbers within each folder.
- All requests under `v1/` inherit auth from their folder (`auth: inherit`). Only override if the endpoint is unauthenticated (`auth: none`).
- Bearer token always uses `{{ecommerce-jwt-token}}` from the environment.
- URL always uses `{{ecommerce-url}}` as the base.
- When adding a new resource, create a `folder.bru` in the new subfolder.

### File format

```bru
meta {
  name: <Operation name>
  type: http
  seq: <N>
}

<method> {
  url: {{ecommerce-url}}/v1/<resource>/<path>
  body: json | none
  auth: bearer | inherit | none
}

params:path {
  <param>: <example-value>
}

params:query {
  ~<param>: <example-value>   # prefix ~ to disable optional params
}

headers {
  Accept-Language: {{accept-language}}
}

auth:bearer {
  token: {{ecommerce-jwt-token}}
}

body:json {
  {
    <json-body>
  }
}

script:post-response {
  // Store response values in env vars when needed
  bru.setEnvVar("product-version", res.getBody().version);
}

settings {
  encodeUrl: true
  timeout: 0
}
```

### When to update Bruno files

| API change                                   | Bruno action                                                           |
|----------------------------------------------|------------------------------------------------------------------------|
| New endpoint added                           | Add a new `.bru` file in the corresponding resource folder             |
| Endpoint URL changed                         | Update the `url` in the affected `.bru` file                           |
| Request body field added/removed             | Update `body:json` in the affected `.bru` file                         |
| Path/query parameter added/removed           | Update `params:path` or `params:query`                                 |
| New resource (new folder in `v1/`)           | Create the resource subfolder + `folder.bru` + one `.bru` per endpoint |
| Endpoint removed                             | Delete the corresponding `.bru` file                                   |
| Response field needed by subsequent requests | Add/update `script:post-response` to store it via `bru.setEnvVar()`    |
