package com.jeferro.ecommerce.products.reviews.domain.events;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;

public class ReviewDeleted extends ReviewEvent {

  public ReviewDeleted(Review entity) {
    super(entity);
  }

  public static ReviewDeleted create(Review review) {
    return new ReviewDeleted(review);
  }
}
