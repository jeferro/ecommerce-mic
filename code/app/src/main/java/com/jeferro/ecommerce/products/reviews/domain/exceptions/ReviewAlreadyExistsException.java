package com.jeferro.ecommerce.products.reviews.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.REVIEW_ALREADY_EXISTS;

import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

public class ReviewAlreadyExistsException extends ValueValidationException {

  protected ReviewAlreadyExistsException(String code, String title, String message) {
    super(code, title, message);
  }

  public static ReviewAlreadyExistsException createOf(ReviewId reviewId) {
    return new ReviewAlreadyExistsException(
        REVIEW_ALREADY_EXISTS, "Review already exists", "Review " + reviewId + " already exists");
  }
}
