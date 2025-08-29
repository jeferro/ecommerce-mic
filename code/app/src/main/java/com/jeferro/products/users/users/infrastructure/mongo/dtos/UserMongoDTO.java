package com.jeferro.products.users.users.infrastructure.mongo.dtos;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "users")
public record UserMongoDTO(
        String id,
        String encodedPassword,
        Set<String> roles
) {
}
