package com.jeferro.ecommerce.products.reviews.domain.events;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import lombok.Getter;

@Getter
public class ReviewUpdated extends ReviewEvent {

  public ReviewUpdated(Review entity) {
    super(entity);
  }

  public static ReviewUpdated create(Review review) {
    return new ReviewUpdated(review);
  }
}
