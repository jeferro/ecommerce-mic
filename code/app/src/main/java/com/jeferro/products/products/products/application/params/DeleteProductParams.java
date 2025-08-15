package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class DeleteProductParams extends Params<Product> {

    private final ProductId id;

    public DeleteProductParams(ProductId id) {
        super();

        ValueValidationUtils.isNotNull(id, "id", this);

        this.id = id;
    }
}
