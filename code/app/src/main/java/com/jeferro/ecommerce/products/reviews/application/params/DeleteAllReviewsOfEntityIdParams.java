package com.jeferro.ecommerce.products.reviews.application.params;

import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import java.util.List;
import lombok.Getter;

@Getter
public class DeleteAllReviewsOfEntityIdParams extends Params<List<ReviewId>> {

  private final EntityId entityId;

  public DeleteAllReviewsOfEntityIdParams(EntityId entityId) {
    super();

    ValueValidator.isNotNull(entityId, "entityId");

    this.entityId = entityId;
  }
}
