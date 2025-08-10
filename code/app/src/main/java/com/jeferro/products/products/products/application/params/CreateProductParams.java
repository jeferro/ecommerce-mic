package com.jeferro.products.products.products.application.params;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CreateProductParams extends Params<Product> {

    private final ProductCode code;

    private final Instant effectiveDate;

    private final ParametricValueId typeId;

    private final LocalizedField name;

    public CreateProductParams(ProductCode code,
                               Instant effectiveDate,
                               ParametricValueId typeId,
                               LocalizedField name) {
        super();
	  this.effectiveDate = effectiveDate;

	  ValueValidationUtils.isNotNull(code, "code", this);
	  ValueValidationUtils.isNotNull(effectiveDate, "effectiveDate", this);
        ValueValidationUtils.isNotNull(typeId, "typeId", this);
        ValueValidationUtils.isNotNull(name, "name", this);

        this.code = code;
        this.typeId = typeId;
        this.name = name;
    }
}
