package com.jeferro.shared.mongo.sequence;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequences")
public record SequenceMongoDocument(String id, String value) {}
