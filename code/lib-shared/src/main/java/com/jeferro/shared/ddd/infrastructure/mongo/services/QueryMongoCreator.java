package com.jeferro.shared.ddd.infrastructure.mongo.services;

import com.jeferro.shared.ddd.domain.models.filter.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public abstract class QueryMongoCreator<Order, F extends Filter<Order>> {

    public Query create(F filter) {
        var query = createQuery(filter);

        var pageable = createPageable(filter);
        query.with(pageable);

        var sort = createSort(filter);
        query.with(sort);

        return query;
    }

    private Sort createSort(F filter) {
        String sortBy = mapOrder(filter.getOrder());
        Sort.Direction sortDirection = filter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(sortDirection, sortBy);
    }

    private Query createQuery(F filter) {
        Query query = new Query();

        mapFilter(filter)
                .forEach(query::addCriteria);

        return query;
    }

    private Pageable createPageable(F filter) {
        int pageNumber = filter.getPageNumber();
        int pageSize = filter.getPageSize();

        return PageRequest.of(pageNumber, pageSize);
    }

    protected abstract List<Criteria> mapFilter(F filter);

    protected abstract String mapOrder(Order order);
}
