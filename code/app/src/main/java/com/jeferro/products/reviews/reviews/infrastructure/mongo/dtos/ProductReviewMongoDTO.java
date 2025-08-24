package com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("product_reviews")
public record ProductReviewMongoDTO(
        String id,
        String username,
        String productCode,
        String locale,
        String comment
) {
    public String getId() {
        return username + "::" + productCode;
    }
}
