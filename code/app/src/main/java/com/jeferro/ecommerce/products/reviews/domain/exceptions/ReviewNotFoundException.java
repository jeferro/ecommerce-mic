package com.jeferro.ecommerce.products.reviews.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.REVIEW_NOT_FOUND;

import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {

  protected ReviewNotFoundException(String code, String message) {
    super(code, "Review not found", message);
  }

  public static ReviewNotFoundException createOf(ReviewId reviewId) {
    return new ReviewNotFoundException(
        REVIEW_NOT_FOUND, "Review " + reviewId + " not found");
  }
}
