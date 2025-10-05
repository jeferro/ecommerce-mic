package com.jeferro.shared.auth.infrastructure.mongo.dtos;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MetadataMongoDTO {

  private Instant createdAt;

  private String createdBy;

  private Instant updatedAt;

  private String updatedBy;

  protected void markAsCreatedBy(String username) {
    var now = Instant.now();

    createdAt = now;
    createdBy = username;

    updatedAt = now;
    updatedBy = username;
  }

  protected void markAsUpdateBy(String username) {
    updatedAt = Instant.now();
    updatedBy = username;
  }

  protected boolean isNew() {
    return createdAt == null;
  }
}
