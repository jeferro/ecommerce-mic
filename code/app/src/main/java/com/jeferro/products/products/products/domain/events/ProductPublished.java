package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.events.EventId;

public class ProductPublished extends ProductEvent {

    private ProductPublished(EventId id,
                             ProductId productId) {
        super(id, productId);
    }

    public static ProductPublished create(Product product) {
        var id = EventId.create();

        var productId = product.getId();

        return new ProductPublished(id, productId);
    }
}
