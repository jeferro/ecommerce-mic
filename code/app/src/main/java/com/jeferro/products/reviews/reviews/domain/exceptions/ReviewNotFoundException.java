package com.jeferro.products.reviews.reviews.domain.exceptions;

import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends NotFoundException {

    protected ReviewNotFoundException(String message) {
        super(REVIEW_NOT_FOUND, "Review not found", message);
    }

    public static ReviewNotFoundException createOf(ReviewId reviewId) {
        return new ReviewNotFoundException("Review " + reviewId + " not found");
    }
}
