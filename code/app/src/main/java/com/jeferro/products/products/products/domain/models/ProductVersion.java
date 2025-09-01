package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.events.*;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.products.products.products.domain.services.InstantTruncator;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static com.jeferro.products.products.products.domain.models.status.ProductStatus.UNPUBLISHED;
import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
public class ProductVersion extends AggregateRoot<ProductVersionId> {

    private LocalizedField name;

    private final ParametricValueId typeId;

    private ProductStatus status;

    private Instant endEffectiveDate;

    public ProductVersion(ProductVersionId id,
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

    public static ProductVersion create(ProductVersionId versionId,
                                 ParametricValueId typeId,
                                 LocalizedField name,
                                 ProductVersion nextVersion) {
        ValueValidator.isNotNull(versionId, "versionId");
        ValueValidator.isNotNull(typeId, "typeId");
        ValueValidator.isNotNull(name, "name");

        if(nextVersion != null) {
            ValueValidator.ensure(() -> nextVersion.hasSameCode(versionId.getCode()),
                "Next product version hasn't belong new product version");
            ValueValidator.ensure(() -> nextVersion.isAfter(versionId.getEffectiveDate()),
                "Next product version is before than new product version");
        }

        var endEffectiveDate = nextVersion != null
            ? nextVersion.getEffectiveDate().minus(1, SECONDS)
            : null;

        var product = new ProductVersion(versionId,
            name,
            typeId,
            InstantTruncator.trunkToSeconds(endEffectiveDate),
            UNPUBLISHED);

        var event = ProductVersionCreated.create(product);
        product.record(event);

        return product;
    }

    public void update(LocalizedField name) {
        ValueValidator.isNotNull(name, "name");

        this.name = name;

        var event = ProductVersionUpdated.create(this);
        record(event);
    }

    public void publish() {
        if (PUBLISHED.equals(status)) {
            return;
        }

        this.status = PUBLISHED;

        var event = ProductVersionPublished.create(this);
        record(event);
    }

    public void unpublish() {
        if (UNPUBLISHED.equals(status)) {
            return;
        }

        this.status = UNPUBLISHED;

        var event = ProductVersionUnpublished.create(this);
        record(event);
    }

    public void delete() {
        var event = ProductVersionDeleted.create(this);
        record(event);
    }

    public void expireBeforeVersion(ProductVersionId versionId) {
        endEffectiveDate = versionId.getEffectiveDate().minus(1, SECONDS);

        var event = ProductVersionUpdated.create(this);
        record(event);
    }

    public void notExpire() {
        endEffectiveDate = null;

        var event = ProductVersionUpdated.create(this);
        record(event);
    }

    @Override
    @Deprecated
    public ProductVersionId getId() {
        return id;
    }

    public ProductVersionId getVersionId() {
        return id;
    }

    public boolean isPublished() {
        return PUBLISHED.equals(status);
    }

    public boolean isUnpublished() {
        return UNPUBLISHED.equals(status);
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
