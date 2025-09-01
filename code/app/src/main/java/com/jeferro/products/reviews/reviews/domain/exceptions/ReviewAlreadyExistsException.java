package com.jeferro.products.reviews.reviews.domain.exceptions;

import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.REVIEW_ALREADY_EXISTS;

public class ReviewAlreadyExistsException extends ValueValidationException {

    protected ReviewAlreadyExistsException(String code, String title, String message) {
        super(code, title, message);
    }

    public static ReviewAlreadyExistsException createOf(ReviewId reviewId) {
        return new ReviewAlreadyExistsException(REVIEW_ALREADY_EXISTS, "Review already exists",
            "Review " + reviewId + " already exists");
    }
}
