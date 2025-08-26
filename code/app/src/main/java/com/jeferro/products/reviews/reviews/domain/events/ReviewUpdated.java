package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import lombok.Getter;

import java.util.Locale;

@Getter
public class ReviewUpdated extends ReviewEvent {

    private final Locale locale;

    private final String comment;

    private ReviewUpdated(ReviewId reviewId,
                                 Locale locale,
                                 String comment) {
        super(reviewId);

        this.locale = locale;
        this.comment = comment;
    }

    public static ReviewUpdated create(Review review) {
        var reviewId = review.getId();
        var locale = review.getLocale();
        var comment = review.getComment();

        return new ReviewUpdated(reviewId, locale, comment);
    }
}
