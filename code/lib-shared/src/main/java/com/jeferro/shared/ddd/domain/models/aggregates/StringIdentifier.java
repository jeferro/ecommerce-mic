package com.jeferro.shared.ddd.domain.models.aggregates;

import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public abstract class StringIdentifier extends Identifier {

    private final String value;

    public StringIdentifier(String value) {
        ValueValidationUtils.isNotNull(value, "value");

        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
