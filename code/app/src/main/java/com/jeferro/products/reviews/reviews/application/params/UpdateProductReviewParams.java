package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class UpdateProductReviewParams extends Params<ProductReview> {

    private final ProductReviewId productReviewId;

    private final String comment;

    public UpdateProductReviewParams(ProductReviewId productReviewId, String comment) {
        super();

        ValueValidationUtils.isNotNull(productReviewId, "productReviewId", this);
        ValueValidationUtils.isNotNull(comment, "comment", this);

        this.productReviewId = productReviewId;
        this.comment = comment;
    }

}
