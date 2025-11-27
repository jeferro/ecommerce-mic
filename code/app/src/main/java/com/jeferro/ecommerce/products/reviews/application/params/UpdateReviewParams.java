package com.jeferro.ecommerce.products.reviews.application.params;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class UpdateReviewParams extends Params<Review> {

  private final ReviewId reviewId;

  private final String comment;

  private final long version;

  public UpdateReviewParams(ReviewId reviewId, String comment, long version) {
    super();

    ValueValidator.isNotNull(reviewId, "reviewId");
    ValueValidator.isNotNull(comment, "comment");

    this.reviewId = reviewId;
    this.comment = comment;
    this.version = version;
  }
}
