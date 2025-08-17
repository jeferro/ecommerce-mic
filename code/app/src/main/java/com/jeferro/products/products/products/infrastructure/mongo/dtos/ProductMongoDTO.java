package com.jeferro.products.products.products.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "products")
public record ProductMongoDTO(
        String id,
		String code,
		Instant effectiveDate,
		Instant endEffectiveDate,
        String typeId,
        ProductStatusMongoDTO status,
        Map<String, String> name
) {
}
