package com.jeferro.ecommerce.users.users.domain.models.criteria;

import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewOrder;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import lombok.Getter;

@Getter
public class UserCriteria extends DomainCriteria<ReviewOrder> {

  public UserCriteria(Integer pageNumber, Integer pageSize, ReviewOrder order, Boolean ascending) {
    super(pageNumber, pageSize, order, ascending);
  }
}
