package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class UnpublishProductParams extends Params<Product> {

    private final ProductId id;

    public UnpublishProductParams(ProductId id) {
        super();

        ValueValidationUtils.isNotNull(id, "id", this);

        this.id = id;
    }
}
