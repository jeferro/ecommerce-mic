package com.jeferro.products.products.infrastructure.mongo.dtos;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public record ProductVersionMongoDTO(
    String id,
    String code,
    Instant effectiveDate,
    Instant endEffectiveDate,
    String typeId,
    ProductStatusMongoDTO status,
    Map<String, String> name) {}
