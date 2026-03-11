package com.jeferro.shared.ddd.domain.models.aggregates;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Metadata extends ValueObject {

  private final Instant createdAt;

  private final String createdBy;

  private final Instant updatedAt;

  private final String updatedBy;
}
