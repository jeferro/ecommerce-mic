package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.shared.ddd.domain.models.filter.Filter;
import lombok.Getter;

import static com.jeferro.products.reviews.reviews.domain.models.ReviewOrder.ID;

@Getter
public class ReviewFilter extends Filter<ReviewOrder> {

    private final EntityId entityId;

    public ReviewFilter(Integer pageNumber,
        Integer pageSize,
        ReviewOrder order,
        Boolean ascending,
        EntityId entityId) {
        super(pageNumber, pageSize, order, ascending);

        this.entityId = entityId;
	}

    public static ReviewFilter byEntityId(EntityId entityId, int pageNumber) {
        return new ReviewFilter(pageNumber, DEFAULT_PAGE_SIZE, ID, null, entityId);
    }

  public boolean hasEntityId() {
        return entityId != null;
    }
}
