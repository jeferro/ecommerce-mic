package com.jeferro.products.reviews.reviews.domain.exceptions;

import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends NotFoundException {

    protected ReviewNotFoundException(String code, String title, String message) {
        super(code, title, message);
    }

    public static ReviewNotFoundException createOf(ReviewId reviewId) {
        return new ReviewNotFoundException(REVIEW_NOT_FOUND, "Review not found",
            "Review " + reviewId + " not found");
    }
}
