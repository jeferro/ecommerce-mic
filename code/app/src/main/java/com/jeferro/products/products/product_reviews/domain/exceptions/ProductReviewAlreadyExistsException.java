package com.jeferro.products.products.product_reviews.domain.exceptions;

import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.REVIEW_ALREADY_EXISTS;

public class ProductReviewAlreadyExistsException extends ValueValidationException {

    protected ProductReviewAlreadyExistsException(String message) {
        super(REVIEW_ALREADY_EXISTS, "Product review already exists", message);
    }

    public static ProductReviewAlreadyExistsException createOf(ProductReviewId productReviewId) {
        return new ProductReviewAlreadyExistsException("Product review " + productReviewId + " already exists");
    }
}
