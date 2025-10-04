package com.jeferro.products.reviews.application.params;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class UpdateReviewParams extends Params<Review> {

  private final ReviewId reviewId;

  private final String comment;

  public UpdateReviewParams(ReviewId reviewId, String comment) {
    super();

    ValueValidator.isNotNull(reviewId, "reviewId");
    ValueValidator.isNotNull(comment, "comment");

    this.reviewId = reviewId;
    this.comment = comment;
  }
}
