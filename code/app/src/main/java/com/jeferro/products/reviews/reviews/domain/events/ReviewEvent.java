package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ReviewEvent extends Event {

    private final ReviewId reviewId;

    protected ReviewEvent(ReviewId reviewId) {
        super();

        this.reviewId = reviewId;
    }

    public ProductCode getProductCode() {
        return reviewId.getEntityId();
    }

    public String getUsername() {
        return reviewId.getUsername();
    }
}
