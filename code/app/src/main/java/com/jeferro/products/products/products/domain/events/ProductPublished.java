package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;

public class ProductPublished extends ProductEvent {

    private ProductPublished(ProductId productId) {
        super(productId);
    }

    public static ProductPublished create(Product product) {
        var id = product.getId();

        return new ProductPublished(id);
    }
}
