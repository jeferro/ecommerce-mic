package com.jeferro.products.reviews.domain.events;

import com.jeferro.products.reviews.domain.models.Review;
import lombok.Getter;

@Getter
public class ReviewCreated extends ReviewEvent {

  public ReviewCreated(Review entity) {
    super(entity);
  }

  public static ReviewCreated create(Review review) {
    return new ReviewCreated(review);
  }
}
