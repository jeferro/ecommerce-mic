package com.jeferro.ecommerce.products.reviews.domain.events;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ReviewEvent extends Event<Review, ReviewId> {

  public ReviewEvent(Review entity) {
    super(entity);
  }
}
