package com.jeferro.products.reviews.application.params;

import java.util.List;

import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
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
