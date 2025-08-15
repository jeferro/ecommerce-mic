package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.events.EventId;

public class ProductUnpublished extends ProductEvent {

    private ProductUnpublished(ProductId productId) {
        super(productId);
    }

    public static ProductUnpublished create(Product product) {
        var id = product.getId();

        return new ProductUnpublished(id);
    }
}
