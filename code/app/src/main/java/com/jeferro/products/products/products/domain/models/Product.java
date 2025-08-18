package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.events.*;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.products.products.products.domain.services.InstantTruncator;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static com.jeferro.products.products.products.domain.models.status.ProductStatus.UNPUBLISHED;
import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
public class Product extends AggregateRoot<ProductId> {

    private LocalizedField name;

    private final ParametricValueId typeId;

    private ProductStatus status;

    private Instant endEffectiveDate;

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
                                 Product nextVersion) {
        ValueValidationUtils.isNotNull(id, "id", Product.class);
        ValueValidationUtils.isNotNull(typeId, "typeId", Product.class);
        ValueValidationUtils.isNotNull(name, "name", Product.class);

        if(nextVersion != null) {
            ValueValidationUtils.ensure(() -> nextVersion.hasSameCode(id.getCode()),
                "Next product version hasn't belong new product version");
            ValueValidationUtils.ensure(() -> nextVersion.isAfter(id.getEffectiveDate()),
                "Next product version is before than new product version");
        }

        var endEffectiveDate = nextVersion != null
            ? nextVersion.getEffectiveDate().minus(1, SECONDS)
            : null;

        var product = new Product(id,
            name,
            typeId,
            InstantTruncator.trunkToSeconds(endEffectiveDate),
            UNPUBLISHED);

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

    public void expireBefore(ProductId productId) {
        endEffectiveDate = productId.getEffectiveDate().minus(1, SECONDS);

        var event = ProductUpdated.create(this);
        record(event);
    }

    public boolean hasSameCode(ProductCode code){
        return getCode().equals(code);
    }

    private Boolean isAfter(Instant effectiveDate) {
        return getEffectiveDate().isAfter(effectiveDate);
    }

    public ProductCode getCode() {
        return id.getCode();
    }

    public Instant getEffectiveDate() {
        return id.getEffectiveDate();
    }
}
