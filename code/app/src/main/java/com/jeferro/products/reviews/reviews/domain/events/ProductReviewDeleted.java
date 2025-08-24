package com.jeferro.products.reviews.reviews.domain.events;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;

public class ProductReviewDeleted extends ProductReviewEvent {

    private ProductReviewDeleted(ProductReviewId productReviewId) {
        super(productReviewId);
    }

    public static ProductReviewDeleted create(ProductReview productReview) {
        var productReviewId = productReview.getId();

        return new ProductReviewDeleted(productReviewId);
    }
}
