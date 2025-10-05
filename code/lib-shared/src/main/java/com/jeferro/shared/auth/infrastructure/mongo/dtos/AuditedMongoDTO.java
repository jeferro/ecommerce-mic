package com.jeferro.shared.auth.infrastructure.mongo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AuditedMongoDTO {

  private MetadataMongoDTO metadata;

  public void markAsSavedBy(String username) {
    if (metadata == null || metadata.isNew()) {
      metadata = MetadataMongoDTO.createOfUser(username);
      return;
    }

    metadata.markAsUpdateBy(username);
  }
}
