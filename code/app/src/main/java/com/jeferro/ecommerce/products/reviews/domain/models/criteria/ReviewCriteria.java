package com.jeferro.ecommerce.products.reviews.domain.models.criteria;

import static com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewOrder.ID;

import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import lombok.Getter;

@Getter
public class ReviewCriteria extends DomainCriteria<ReviewOrder> {

  private final EntityId entityId;

  public ReviewCriteria(Integer pageNumber, Integer pageSize, ReviewOrder order, Boolean ascending, EntityId entityId) {
    super(pageNumber, pageSize, order, ascending);

    this.entityId = entityId;
  }

  public static ReviewCriteria allByEntityId(EntityId entityId) {
    return new ReviewCriteria(null, null, null, null, entityId);
  }

  public static ReviewCriteria byEntityIdPage(EntityId entityId, int pageNumber, int pageSize) {
    return new ReviewCriteria(pageNumber, pageSize, ID, null, entityId);
  }

  public boolean hasEntityId() {
    return entityId != null;
  }
}
