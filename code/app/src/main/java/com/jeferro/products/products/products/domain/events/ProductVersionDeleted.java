package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ProductVersionDeleted extends ProductVersionEvent {

    private final LocalizedField name;

    private final ProductStatus status;

    private ProductVersionDeleted(ProductVersionId versionId,
                           LocalizedField name,
                           ProductStatus status) {
        super(versionId);

        this.name = name;
        this.status = status;
    }

    public static ProductVersionDeleted create(ProductVersion productVersion) {
        var versionId = productVersion.getVersionId();
        var name = productVersion.getName();
        var status = productVersion.getStatus();

        return new ProductVersionDeleted(versionId, name, status);
    }
}
