package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class UpdateReviewParams extends Params<Review> {

    private final ReviewId reviewId;

    private final String comment;

    public UpdateReviewParams(ReviewId reviewId, String comment) {
        super();

        ValueValidationUtils.isNotNull(reviewId, "reviewId");
        ValueValidationUtils.isNotNull(comment, "comment");

        this.reviewId = reviewId;
        this.comment = comment;
    }

}
