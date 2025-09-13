package com.jeferro.products.reviews.domain.events;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import lombok.Getter;

import java.util.Locale;

@Getter
public class ReviewCreated extends ReviewEvent {

    private final Locale locale;

    private final String comment;

    private ReviewCreated(ReviewId reviewId,
                                 Locale locale,
                                 String comment) {
        super(reviewId);

        this.locale = locale;
        this.comment = comment;
    }

    public static ReviewCreated create(Review review) {
        var reviewId = review.getId();
        var locale = review.getLocale();
        var comment = review.getComment();

        return new ReviewCreated(reviewId, locale, comment);
    }
}
