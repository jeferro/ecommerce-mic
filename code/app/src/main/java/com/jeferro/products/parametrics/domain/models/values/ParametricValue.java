package com.jeferro.products.parametrics.domain.models.values;

import com.jeferro.shared.ddd.domain.models.projection.Projection;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ParametricValue extends Projection<ParametricValueId> {

    private final LocalizedField name;

    private final String value;

    public ParametricValue(
            ParametricValueId id,
            LocalizedField name,
            String value) {
        super(id);

        this.name = name;
        this.value = value;
    }
}
