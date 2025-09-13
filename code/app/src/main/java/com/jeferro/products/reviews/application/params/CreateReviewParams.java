package com.jeferro.products.reviews.application.params;

import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class CreateReviewParams extends Params<Review> {

    private final EntityId entityId;

    private final String comment;

    public CreateReviewParams(EntityId entityId, String comment) {
        super();

        ValueValidator.isNotNull(entityId, "entityId");
        ValueValidator.isNotNull(comment, "comment");

        this.entityId = entityId;
        this.comment = comment;
    }

}
