package com.jeferro.products.users.infrastructure.mongo.dtos;

import java.util.Set;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record UserMongoDTO(String id, String encodedPassword, Set<String> roles) {}
