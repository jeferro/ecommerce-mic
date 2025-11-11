package com.jeferro.products.reviews.domain.events;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ReviewEvent extends Event<Review, ReviewId> {

  public ReviewEvent(Review entity) {
    super(entity);
  }
}
