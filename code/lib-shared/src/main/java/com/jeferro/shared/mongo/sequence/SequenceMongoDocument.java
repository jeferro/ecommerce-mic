package com.jeferro.shared.mongo.sequence;

import java.time.Instant;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "sequences")
public class SequenceMongoDocument extends AuditedMongoDTO {

  private final String id;

  private final String value;

  public SequenceMongoDocument(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy,
      String id,
      String value) {
    super(createdAt,
        createdBy,
        updatedAt,
        updatedBy);
    this.id = id;
    this.value = value;
  }
}
