package com.jeferro.shared.ddd.domain.models.aggregates;

import com.jeferro.shared.ddd.domain.services.ValueValidator;

public abstract class StringIdentifier extends Identifier {

  private final String value;

  public StringIdentifier(String value) {
    ValueValidator.ensureIsNotNull(value, "value");

    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
