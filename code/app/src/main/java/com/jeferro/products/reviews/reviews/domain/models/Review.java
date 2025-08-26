package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.products.reviews.reviews.domain.events.ReviewCreated;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.events.ReviewUpdated;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewDoesNotBelongUserException;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

import java.util.Locale;

@Getter
public class Review extends AggregateRoot<ReviewId> {

    private Locale locale;

    private String comment;

    public Review(ReviewId id,
                         Locale locale,
                         String comment) {
        super(id);

        this.locale = locale;
        this.comment = comment;
    }

    public static Review createOf(ReviewId reviewId,
                                  Locale locale,
                                  String comment) {
        ValueValidationUtils.isNotNull(reviewId, "reviewId", Review.class);
        ValueValidationUtils.isNotNull(locale, "locale", Review.class);
        ValueValidationUtils.isNotNull(comment, "comment", Review.class);

        var review = new Review(reviewId, locale, comment);

        var event = ReviewCreated.create(review);
        review.record(event);

        return review;
    }

    public void update(String comment, Locale locale) {
        ValueValidationUtils.isNotNull(comment, "comment", this);
        ValueValidationUtils.isNotNull(locale, "locale", this);

        this.comment = comment;
        this.locale = locale;

        var event = ReviewUpdated.create(this);
        record(event);
    }

    public void deleteByUser() {
        var event = ReviewDeleted.create(this);
        record(event);
    }

    public void deleteBySystem() {
        var event = ReviewDeleted.create(this);
        record(event);
    }

    public String getUsername() {
        return id.getUsername();
    }

    public EntityId getEntityId() {
        return id.getEntityId();
    }

    public void ensureReviewBelongsToUser(Auth auth) {
        if (auth.username().equals(getUsername())) {
            return;
        }

        throw ReviewDoesNotBelongUserException.belongsToOtherUser(getId(), auth);
    }
}
