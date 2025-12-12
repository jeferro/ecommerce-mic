package com.jeferro.ecommerce.products.reviews.application.params;

import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class CreateReviewParams extends Params<Review> {

  private final EntityId entityId;

  private final String comment;

  public CreateReviewParams(EntityId entityId, String comment) {
    super();

    ValueValidator.ensureIsNotNull(entityId, "entityId");
    ValueValidator.ensureIsNotNull(comment, "comment");

    this.entityId = entityId;
    this.comment = comment;
  }
}
