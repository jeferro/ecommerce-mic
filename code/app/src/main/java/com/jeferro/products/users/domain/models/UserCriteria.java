package com.jeferro.products.users.domain.models;

import com.jeferro.products.reviews.domain.models.ReviewOrder;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import lombok.Getter;

@Getter
public class UserCriteria extends DomainCriteria<ReviewOrder> {

    public UserCriteria(Integer pageNumber,
        Integer pageSize,
        ReviewOrder order,
        Boolean ascending) {
        super(pageNumber, pageSize, order, ascending);
	}
}
