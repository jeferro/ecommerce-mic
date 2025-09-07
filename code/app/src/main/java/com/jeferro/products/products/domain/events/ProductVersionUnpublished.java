package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;

public class ProductVersionUnpublished extends ProductVersionEvent {

    private ProductVersionUnpublished(ProductVersionId versionId) {
        super(versionId);
    }

    public static ProductVersionUnpublished create(ProductVersion productVersion) {
        var versionId = productVersion.getVersionId();

        return new ProductVersionUnpublished(versionId);
    }
}
