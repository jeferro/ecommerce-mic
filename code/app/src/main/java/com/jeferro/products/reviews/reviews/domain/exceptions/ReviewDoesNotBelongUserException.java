package com.jeferro.products.reviews.reviews.domain.exceptions;

import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;

public class ReviewDoesNotBelongUserException extends ForbiddenException {

    protected ReviewDoesNotBelongUserException(String message) {
        super(message);
    }

    public static ReviewDoesNotBelongUserException belongsToOtherUser(ReviewId reviewId, Auth auth) {
        return new ReviewDoesNotBelongUserException("Review " + reviewId + " don't belong to user " + auth.username());
    }
}
