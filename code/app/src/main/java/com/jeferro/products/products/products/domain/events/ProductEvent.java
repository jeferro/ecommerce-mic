package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.events.Event;
import com.jeferro.shared.ddd.domain.events.EventId;
import lombok.Getter;

@Getter
public abstract class ProductEvent extends Event {

    private final ProductId productId;

    protected ProductEvent(EventId id, ProductId productId) {
        super(id);

        this.productId = productId;
    }
}
