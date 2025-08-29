package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;

public class ReviewDeleted extends ReviewEvent {

    private ReviewDeleted(ReviewId reviewId) {
        super(reviewId);
    }

    public static ReviewDeleted create(Review review) {
        var reviewId = review.getId();

        return new ReviewDeleted(reviewId);
    }
}
