package com.jeferro.products.products.product_reviews.domain.exceptions;

import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;

public class ProductReviewDoesNotBelongUserException extends ForbiddenException {

    protected ProductReviewDoesNotBelongUserException(String message) {
        super(message);
    }

    public static ProductReviewDoesNotBelongUserException belongsToOtherUser(ProductReviewId productReviewId, Auth auth) {
        return new ProductReviewDoesNotBelongUserException("Product review " + productReviewId + " don't belong to user " + auth.username());
    }
}
