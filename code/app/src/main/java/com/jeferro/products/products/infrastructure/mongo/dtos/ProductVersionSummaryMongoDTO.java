package com.jeferro.products.products.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "products")
public record ProductVersionSummaryMongoDTO(
        String id,
		Map<String, String> name,
	    ProductStatusMongoDTO status
) {
}
