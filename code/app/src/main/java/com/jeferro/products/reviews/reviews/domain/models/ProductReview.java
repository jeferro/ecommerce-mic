package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.products.reviews.reviews.domain.events.ProductReviewCreated;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewUpdated;
import com.jeferro.products.reviews.reviews.domain.exceptions.ProductReviewDoesNotBelongUserException;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

import java.util.Locale;

@Getter
public class ProductReview extends AggregateRoot<ProductReviewId> {

    private Locale locale;

    private String comment;

    public ProductReview(ProductReviewId id,
                         Locale locale,
                         String comment) {
        super(id);

        this.locale = locale;
        this.comment = comment;
    }

    public static ProductReview createOf(ProductCode productCode,
                                         Locale locale,
                                         String comment,
                                         Auth auth) {
        ValueValidationUtils.isNotNull(productCode, "productCode", ProductReview.class);
        ValueValidationUtils.isNotNull(locale, "locale", ProductReview.class);
        ValueValidationUtils.isNotNull(comment, "comment", ProductReview.class);

        var productReviewId = ProductReviewId.createOf(auth, productCode);

        var productReview = new ProductReview(productReviewId, locale, comment);

        var event = ProductReviewCreated.create(productReview);
        productReview.record(event);

        return productReview;
    }

    public void update(String comment, Locale locale, Auth auth) {
        ValueValidationUtils.isNotNull(locale, "locale", this);
        ValueValidationUtils.isNotNull(comment, "comment", this);

        ensureProductReviewBelongsToUser(auth);

        this.comment = comment;
        this.locale = locale;

        var event = ProductReviewUpdated.create(this);
        record(event);
    }

    public void deleteByUser(Auth auth) {
        ensureProductReviewBelongsToUser(auth);

        var event = ProductReviewDeleted.create(this);
        record(event);
    }

    public void deleteBySystem() {
        var event = ProductReviewDeleted.create(this);
        record(event);
    }

    public String getUsername() {
        return id.getUsername();
    }

    public ProductCode getProductCode() {
        return id.getProductCode();
    }

    private void ensureProductReviewBelongsToUser(Auth auth) {
        if (auth.username().equals(id.getUsername())) {
            return;
        }

        throw ProductReviewDoesNotBelongUserException.belongsToOtherUser(id, auth);
    }
}
