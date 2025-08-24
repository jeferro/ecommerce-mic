package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class DeleteProductReviewParams extends Params<ProductReview> {

    private final ProductReviewId productReviewId;

    public DeleteProductReviewParams(ProductReviewId productReviewId) {
        super();

        ValueValidationUtils.isNotNull(productReviewId, "productReviewId", this);

        this.productReviewId = productReviewId;
    }

}
