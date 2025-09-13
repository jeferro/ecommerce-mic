package com.jeferro.products.reviews.application.params;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class DeleteReviewParams extends Params<Review> {

    private final ReviewId reviewId;

    public DeleteReviewParams(ReviewId reviewId) {
        super();

        ValueValidator.isNotNull(reviewId, "reviewId");

        this.reviewId = reviewId;
    }

}
