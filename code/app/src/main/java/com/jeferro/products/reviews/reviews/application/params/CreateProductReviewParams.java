package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class CreateProductReviewParams extends Params<ProductReview> {

    private final ProductCode productCode;

    private final String comment;

    public CreateProductReviewParams(ProductCode productCode, String comment) {
        super();

        ValueValidationUtils.isNotNull(productCode, "productCode", this);
        ValueValidationUtils.isNotNull(comment, "comment", this);

        this.productCode = productCode;
        this.comment = comment;
    }

}
