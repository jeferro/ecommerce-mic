package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.shared.domain.models.auth.AuthMother;

import java.util.Locale;

public abstract class ProductReviewMother {

    public static ProductReview johnReviewOfApple() {
        var appleCode = ProductCodeMother.apple();
        var johnAuth = AuthMother.john();

        var productReviewId = ProductReviewId.createOf(johnAuth, appleCode);

        return new ProductReview(productReviewId, Locale.US, "Comment about apple");
    }

    public static ProductReview emilyReviewOfApple() {
        var appleCode = ProductCodeMother.apple();
        var emilyAuth = AuthMother.emily();

        var productReviewId = ProductReviewId.createOf(emilyAuth, appleCode);

        return new ProductReview(productReviewId, Locale.US, "I love apples");
    }

    public static ProductReview jamesReviewOfApple() {
        var appleCode = ProductCodeMother.apple();
        var jamesAuth = AuthMother.james();

        var productReviewId = ProductReviewId.createOf(jamesAuth, appleCode);

        return new ProductReview(productReviewId, Locale.US, "I hate apples");
    }
}
