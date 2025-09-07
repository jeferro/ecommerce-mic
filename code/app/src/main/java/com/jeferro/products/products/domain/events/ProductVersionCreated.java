package com.jeferro.products.products.domain.events;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ProductVersionCreated extends ProductVersionEvent {

    private final LocalizedField name;

    private final ParametricValueId typeId;

    private final ProductStatus status;

    private ProductVersionCreated(ProductVersionId versionId,
                           LocalizedField name,
                           ParametricValueId typeId,
                           ProductStatus status) {
        super(versionId);

        this.name = name;
        this.typeId = typeId;
        this.status = status;
    }

    public static ProductVersionCreated create(ProductVersion productVersion) {
        var versionId = productVersion.getVersionId();
        var typeId = productVersion.getTypeId();
        var name = productVersion.getName();
        var status = productVersion.getStatus();

        return new ProductVersionCreated(versionId, name, typeId, status);
    }
}
