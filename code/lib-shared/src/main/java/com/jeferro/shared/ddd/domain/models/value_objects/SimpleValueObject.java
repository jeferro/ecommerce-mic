package com.jeferro.shared.ddd.domain.models.value_objects;

import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SimpleValueObject<T extends Serializable> extends ValueObject {

    private final T value;

    public SimpleValueObject(T value) {
        ValueValidationUtils.isNotNull(value, "value");

        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
