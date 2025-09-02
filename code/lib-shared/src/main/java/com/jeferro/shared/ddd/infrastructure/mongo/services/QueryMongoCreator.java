package com.jeferro.shared.ddd.infrastructure.mongo.services;

import com.jeferro.shared.ddd.domain.models.filter.Criteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public abstract class QueryMongoCreator<Order, C extends Criteria<Order>> {

    public Query create(C filter) {
        var query = createQuery(filter);

        var pageable = createPageable(filter);
        query.with(pageable);

        var sort = createSort(filter);
        query.with(sort);

        return query;
    }

    private Sort createSort(C criteria) {
        String sortBy = mapOrder(criteria.getOrder());
        Sort.Direction sortDirection = criteria.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(sortDirection, sortBy);
    }

    private Query createQuery(C criteria) {
        Query query = new Query();

        mapCriteria(criteria)
                .forEach(query::addCriteria);

        return query;
    }

    private Pageable createPageable(C criteria) {
        int pageNumber = criteria.getPageNumber();
        int pageSize = criteria.getPageSize();

        return PageRequest.of(pageNumber, pageSize);
    }

    protected abstract List<org.springframework.data.mongodb.core.query.Criteria> mapCriteria(C criteria);

    protected abstract String mapOrder(Order order);
}
