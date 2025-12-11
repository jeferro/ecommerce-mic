package com.jeferro.ecommerce.products.reviews.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.REVIEW_NOT_ALLOWED;

import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;

public class ReviewDoesNotBelongUserException extends ForbiddenException {

  protected ReviewDoesNotBelongUserException(String code, String title, String message) {
    super(code, message);
  }

  public static ReviewDoesNotBelongUserException belongsToOtherUser(ReviewId reviewId, Auth auth) {
    return new ReviewDoesNotBelongUserException(
        REVIEW_NOT_ALLOWED,
        "Review not allowed",
        "Review " + reviewId + " don't belong to user " + auth.getUsername());
  }
}
