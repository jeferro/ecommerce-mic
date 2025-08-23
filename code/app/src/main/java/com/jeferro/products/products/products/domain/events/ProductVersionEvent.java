package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ProductVersionEvent extends Event {

    private final ProductVersionId versionId;

    protected ProductVersionEvent(ProductVersionId versionId) {
        super();

        this.versionId = versionId;
    }
}
