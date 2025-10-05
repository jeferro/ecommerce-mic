package com.jeferro.shared.auth.infrastructure.mongo.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AuditedMongoDTO {

  private final MetadataMongoDTO metadata;

  public void markAsSavedBy(String username) {
    if (metadata.isNew()) {
      metadata.markAsCreatedBy(username);
      return;
    }

    metadata.markAsUpdateBy(username);
  }
}
