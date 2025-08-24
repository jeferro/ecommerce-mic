package com.jeferro.products.reviews.reviews.domain.exceptions;

import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.REVIEW_NOT_FOUND;

public class ProductReviewNotFoundException extends NotFoundException {

    protected ProductReviewNotFoundException(String message) {
        super(REVIEW_NOT_FOUND, "Product Review not found", message);
    }

    public static ProductReviewNotFoundException createOf(ProductReviewId productReviewId) {
        return new ProductReviewNotFoundException("Product review " + productReviewId + " not found");
    }
}
