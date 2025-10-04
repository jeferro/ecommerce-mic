package com.jeferro.products.reviews.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("reviews")
public record ReviewMongoDTO(
    String id, String username, EntityIdMongoDTO entityId, String locale, String comment) {}
