package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.ddd.domain.events.EventId;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ProductDeleted extends ProductEvent {

    private final LocalizedField name;

    private final ProductStatus status;

    private ProductDeleted(EventId id,
                           ProductId productId,
                           LocalizedField name,
                           ProductStatus status) {
        super(id, productId);

        this.name = name;
        this.status = status;
    }

    public static ProductDeleted create(Product product) {
        var id = EventId.create();

        var productId = product.getId();
        var name = product.getName();
        var status = product.getStatus();

        return new ProductDeleted(id, productId, name, status);
    }
}
