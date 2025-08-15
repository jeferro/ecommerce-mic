package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class UpdateProductParams extends Params<Product> {

    private final ProductId id;

    private final LocalizedField name;

    public UpdateProductParams(ProductId id, LocalizedField name) {
        super();

        ValueValidationUtils.isNotNull(id, "id", this);
        ValueValidationUtils.isNotNull(name, "name", this);

        this.id = id;
        this.name = name;
    }
}
