package com.jeferro.shared.ddd.domain.models.aggregates;

import java.time.Instant;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.Getter;

@Getter
public class Metadata extends ValueObject {

  private final Instant createdAt;

  private final String createdBy;

  private final Instant updatedAt;

  private final String updatedBy;

  public Metadata(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy) {
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }
}
