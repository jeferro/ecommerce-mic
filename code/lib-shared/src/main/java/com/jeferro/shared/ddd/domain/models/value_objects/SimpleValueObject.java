package com.jeferro.shared.ddd.domain.models.value_objects;

import com.jeferro.shared.ddd.domain.services.ValueValidator;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class SimpleValueObject<T extends Serializable> extends ValueObject {

  private final T value;

  public SimpleValueObject(T value) {
    ValueValidator.isNotNull(value, "value");

    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
