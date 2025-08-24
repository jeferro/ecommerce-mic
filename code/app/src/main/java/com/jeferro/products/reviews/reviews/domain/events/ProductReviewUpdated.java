package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import lombok.Getter;

import java.util.Locale;

@Getter
public class ProductReviewUpdated extends ProductReviewEvent {

    private final Locale locale;

    private final String comment;

    private ProductReviewUpdated(ProductReviewId productReviewId,
                                 Locale locale,
                                 String comment) {
        super(productReviewId);

        this.locale = locale;
        this.comment = comment;
    }

    public static ProductReviewUpdated create(ProductReview productReview) {
        var productReviewId = productReview.getId();
        var locale = productReview.getLocale();
        var comment = productReview.getComment();

        return new ProductReviewUpdated(productReviewId, locale, comment);
    }
}
