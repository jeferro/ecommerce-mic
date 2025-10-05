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

  protected static MetadataMongoDTO createOfUser(String username) {
    var now = Instant.now();

    return new MetadataMongoDTO(now, username, now, username);
  }

  protected void markAsUpdateBy(String username) {
    updatedAt = Instant.now();
    updatedBy = username;
  }

  public boolean isNew() {
    return createdAt == null;
  }
}
