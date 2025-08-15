package com.jeferro.products.products.products.domain.events;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ProductCreated extends ProductEvent {

    private final LocalizedField name;

    private final ParametricValueId typeId;

    private final ProductStatus status;

    private ProductCreated(ProductId id,
                           LocalizedField name,
                           ParametricValueId typeId,
                           ProductStatus status) {
        super(id);

        this.name = name;
        this.typeId = typeId;
        this.status = status;
    }

    public static ProductCreated create(Product product) {
        var id = product.getId();
        var typeId = product.getTypeId();
        var name = product.getName();
        var status = product.getStatus();

        return new ProductCreated(id, name, typeId, status);
    }
}
