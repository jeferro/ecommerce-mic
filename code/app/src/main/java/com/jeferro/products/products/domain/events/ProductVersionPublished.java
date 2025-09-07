package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;

public class ProductVersionPublished extends ProductVersionEvent {

    private ProductVersionPublished(ProductVersionId versionId) {
        super(versionId);
    }

    public static ProductVersionPublished create(ProductVersion productVersion) {
        var versionId = productVersion.getVersionId();

        return new ProductVersionPublished(versionId);
    }
}
