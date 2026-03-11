package com.jeferro.shared.auth.infrastructure.mongo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Version;

@Getter
@AllArgsConstructor
public abstract class AuditedMongoDTO {

  @Version
  private Long version;

  private MetadataMongoDTO metadata;

  public void markAsSavedBy(String username) {
    if (metadata == null || metadata.isNew()) {
      metadata = MetadataMongoDTO.createOfUser(username);
      return;
    }

    metadata.markAsUpdateBy(username);
  }

  public boolean isNew() {
    return metadata == null
        || metadata.getCreatedAt() == null
        || metadata.getCreatedBy() == null;
  }
}
