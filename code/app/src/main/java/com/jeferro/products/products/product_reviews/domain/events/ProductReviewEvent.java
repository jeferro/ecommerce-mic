package com.jeferro.products.products.product_reviews.domain.events;

import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ProductReviewEvent extends Event {

    private final ProductReviewId productReviewId;

    protected ProductReviewEvent(ProductReviewId productReviewId) {
        super();

        this.productReviewId = productReviewId;
    }

    public ProductCode getProductCode() {
        return productReviewId.getProductCode();
    }

    public String getUsername() {
        return productReviewId.getUsername();
    }
}
