package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class DeleteAllProductReviewsOfProductParams extends Params<Void> {

    private final ProductCode productCode;

    public DeleteAllProductReviewsOfProductParams(ProductCode productCode) {
        super();

        ValueValidationUtils.isNotNull(productCode, "productCode", this);

        this.productCode = productCode;
    }

}
