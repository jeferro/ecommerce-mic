package com.jeferro.products.reviews.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("reviews")
public record EntityIdMongoDTO(String domain, String id) {}
