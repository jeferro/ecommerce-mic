package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class CreateReviewParams extends Params<Review> {

    private final EntityId entityId;

    private final String comment;

    public CreateReviewParams(EntityId entityId, String comment) {
        super();

        ValueValidationUtils.isNotNull(entityId, "entityId", this);
        ValueValidationUtils.isNotNull(comment, "comment", this);

        this.entityId = entityId;
        this.comment = comment;
    }

}
