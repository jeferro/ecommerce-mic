package com.jeferro.products.products.domain.models;

import com.jeferro.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ProductVersionSummary extends AggregateRoot<ProductVersionId> {

    protected LocalizedField name;

    protected ProductStatus status;

    public ProductVersionSummary(ProductVersionId id,
                   LocalizedField name,
                   ProductStatus status) {
        super(id);

        this.name = name;
    }

    @Override
    @Deprecated
    public ProductVersionId getId() {
        return id;
    }

    public ProductVersionId getVersionId() {
        return id;
    }

    public ProductCode getCode() {
        return id.getCode();
    }

    public Instant getEffectiveDate() {
        return id.getEffectiveDate();
    }
}
