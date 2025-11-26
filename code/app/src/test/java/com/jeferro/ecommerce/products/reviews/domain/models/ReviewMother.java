package com.jeferro.ecommerce.products.reviews.domain.models;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCodeMother;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import java.util.Locale;

public abstract class ReviewMother {

  public static Review johnReviewOfApple() {
    var entityId = EntityId.createOf("products", ProductCodeMother.apple().toString());
    var johnAuth = AuthMother.john();

    var productReviewId = ReviewId.createOf(entityId, johnAuth);

    return new Review(productReviewId, "Comment about apple", Locale.US, 1, null);
  }

  public static Review emilyReviewOfApple() {
    var entityId = EntityId.createOf("products", ProductCodeMother.apple().toString());
    var emilyAuth = AuthMother.emily();

    var productReviewId = ReviewId.createOf(entityId, emilyAuth);

    return new Review(productReviewId, "I love apples", Locale.US, 1, null);
  }

  public static Review jamesReviewOfApple() {
    var entityId = EntityId.createOf("products", ProductCodeMother.apple().toString());
    var jamesAuth = AuthMother.james();

    var productReviewId = ReviewId.createOf(entityId, jamesAuth);

    return new Review(productReviewId, "I hate apples", Locale.US, 1, null);
  }
}
