package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class GetReviewParams extends Params<Review> {

    private final ReviewId reviewId;

    public GetReviewParams(ReviewId reviewId) {
        super();

        ValueValidationUtils.isNotNull(reviewId, "reviewId", this);

        this.reviewId = reviewId;
    }

}
