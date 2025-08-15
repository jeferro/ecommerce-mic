package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.events.*;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static com.jeferro.products.products.products.domain.models.status.ProductStatus.UNPUBLISHED;

@Getter
public class Product extends AggregateRoot<ProductId> {

    private LocalizedField name;

    private final ParametricValueId typeId;

    private ProductStatus status;

    public Product(ProductId id,
                   LocalizedField name,
                   ParametricValueId typeId,
                   ProductStatus status) {
        super(id);

        this.name = name;
        this.typeId = typeId;
        this.status = status;
    }

    public static Product create(ProductId id,
                                 ParametricValueId typeId,
                                 LocalizedField name) {
        ValueValidationUtils.isNotNull(id, "id", Product.class);
        ValueValidationUtils.isNotNull(typeId, "typeId", Product.class);
        ValueValidationUtils.isNotNull(name, "name", Product.class);

        var product = new Product(id, name, typeId, UNPUBLISHED);

        var event = ProductCreated.create(product);
        product.record(event);

        return product;
    }

    public void update(LocalizedField name) {
        ValueValidationUtils.isNotNull(name, "name", this);

        this.name = name;

        var event = ProductUpdated.create(this);
        record(event);
    }

    public void publish() {
        if (PUBLISHED.equals(status)) {
            return;
        }

        this.status = PUBLISHED;

        var event = ProductPublished.create(this);
        record(event);
    }

    public void unpublish() {
        if (UNPUBLISHED.equals(status)) {
            return;
        }

        this.status = UNPUBLISHED;

        var event = ProductUnpublished.create(this);
        record(event);
    }

    public void delete() {
        var event = ProductDeleted.create(this);
        record(event);
    }

    public ProductCode getCode() {
        return id.getCode();
    }

    public Instant getStartEffectiveDate() {
        return id.getStartEffectiveDate();
    }
}
