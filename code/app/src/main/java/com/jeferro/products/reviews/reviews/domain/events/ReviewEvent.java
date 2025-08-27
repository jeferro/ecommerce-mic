package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ReviewEvent extends Event {

    private final ReviewId reviewId;

    protected ReviewEvent(ReviewId reviewId) {
        super();

        this.reviewId = reviewId;
    }

    public EntityId getEntityId() {
        return reviewId.getEntityId();
    }

    public String getUsername() {
        return reviewId.getUsername();
    }
}
