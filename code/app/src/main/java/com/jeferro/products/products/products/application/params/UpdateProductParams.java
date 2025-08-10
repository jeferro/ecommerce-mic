package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class UpdateProductParams extends Params<Product> {

    private final ProductId productId;

    private final LocalizedField name;

    public UpdateProductParams(ProductId productId, LocalizedField name) {
        super();

        ValueValidationUtils.isNotNull(productId, "productId", this);
        ValueValidationUtils.isNotNull(name, "name", this);

        this.productId = productId;
        this.name = name;
    }
}
