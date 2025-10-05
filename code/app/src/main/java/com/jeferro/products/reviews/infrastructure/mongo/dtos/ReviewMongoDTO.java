package com.jeferro.products.reviews.infrastructure.mongo.dtos;

import java.time.Instant;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("reviews")
public class ReviewMongoDTO extends AuditedMongoDTO {

  private final String id;

  private final String username;

  private final EntityIdMongoDTO entityId;

  private final String locale;

  private final String comment;

  public ReviewMongoDTO(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy,
      String id,
      String username,
      EntityIdMongoDTO entityId,
      String locale,
      String comment) {
    super(createdAt,
        createdBy,
        updatedAt,
        updatedBy);
    this.id = id;
    this.username = username;
    this.entityId = entityId;
    this.locale = locale;
    this.comment = comment;
  }
}
