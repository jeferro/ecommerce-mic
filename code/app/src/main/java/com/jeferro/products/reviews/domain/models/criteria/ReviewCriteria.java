package com.jeferro.products.reviews.domain.models.criteria;

import static com.jeferro.products.reviews.domain.models.criteria.ReviewOrder.ID;

import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import lombok.Getter;

@Getter
public class ReviewCriteria extends DomainCriteria<ReviewOrder> {

  private final EntityId entityId;

  public ReviewCriteria(
      Integer pageNumber,
      Integer pageSize,
      ReviewOrder order,
      Boolean ascending,
      EntityId entityId) {
    super(pageNumber, pageSize, order, ascending);

    this.entityId = entityId;
  }

  public static ReviewCriteria byEntityId(EntityId entityId) {
    return new ReviewCriteria(0, DEFAULT_PAGE_SIZE, ID, null, entityId);
  }

  public boolean hasEntityId() {
    return entityId != null;
  }
}
