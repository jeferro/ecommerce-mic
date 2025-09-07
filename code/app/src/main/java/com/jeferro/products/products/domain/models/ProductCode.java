package com.jeferro.products.products.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;

public class ProductCode extends StringIdentifier {

    public ProductCode(String value) {
        super(value);
    }
}
