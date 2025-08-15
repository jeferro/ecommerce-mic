package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ProductEvent extends Event {

    private final ProductId id;

    protected ProductEvent(ProductId id) {
        super();

        this.id = id;
    }
}
