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

    private final Instant endEffectiveDate;

    public Product(ProductId id,
                   LocalizedField name,
                   ParametricValueId typeId,
                   Instant endEffectiveDate,
                   ProductStatus status) {
        super(id);

        this.name = name;
        this.typeId = typeId;
        this.status = status;
        this.endEffectiveDate = endEffectiveDate;
    }

    public static Product create(ProductId id,
                                 ParametricValueId typeId,
                                 LocalizedField name,
                                 Product nextProduct) {
        ValueValidationUtils.isNotNull(id, "id", Product.class);
        ValueValidationUtils.isNotNull(typeId, "typeId", Product.class);
        ValueValidationUtils.isNotNull(name, "name", Product.class);
        ValueValidationUtils.ensure(() -> nextProduct == null || nextProduct.hasSameCode(id.getCode()),
            "Next product version hasn't belong new product version");

        var endEffectiveDate = nextProduct != null ? nextProduct.getEffectiveDate() : null;

        var product = new Product(id, name, typeId, endEffectiveDate, UNPUBLISHED);

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

    public boolean hasSameCode(ProductCode code){
        return getCode().equals(code);
    }

    public ProductCode getCode() {
        return id.getCode();
    }

    public Instant getEffectiveDate() {
        return id.getEffectiveDate();
    }
}
