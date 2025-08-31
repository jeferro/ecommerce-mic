package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class DeleteAllReviewsOfEntityIdParams extends Params<Void> {

    private final EntityId entityId;

    public DeleteAllReviewsOfEntityIdParams(EntityId entityId) {
        super();

        ValueValidator.isNotNull(entityId, "entityId");

        this.entityId = entityId;
    }

}
