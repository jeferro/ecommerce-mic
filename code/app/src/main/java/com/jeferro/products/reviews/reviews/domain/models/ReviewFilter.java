package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.shared.ddd.domain.models.filter.Filter;
import lombok.Getter;

import static com.jeferro.products.reviews.reviews.domain.models.ReviewOrder.ID;

@Getter
public class ReviewFilter extends Filter<ReviewOrder> {

    private final String domain;

    private final String id;

    public ReviewFilter(Integer pageNumber,
        Integer pageSize,
        ReviewOrder order,
        Boolean ascending,
        String domain,
        String id) {
        super(pageNumber, pageSize, order, ascending);

        this.domain = domain;
        this.id = id;
	}

    public static ReviewFilter byEntityId(EntityId entityId, int pageNumber) {
        return new ReviewFilter(pageNumber, DEFAULT_PAGE_SIZE, ID, null, entityId.getDomain(), entityId.getId());
    }

    public boolean hasDomain() {
        return domain != null;
    }

    public boolean hasId() {
        return id != null;
    }
}
