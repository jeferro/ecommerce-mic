package com.jeferro.products.reviews.domain.events;

import com.jeferro.products.reviews.domain.models.Review;
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
