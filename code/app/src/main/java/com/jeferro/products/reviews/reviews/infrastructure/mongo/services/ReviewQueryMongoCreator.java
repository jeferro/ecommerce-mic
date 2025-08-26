package com.jeferro.products.reviews.reviews.infrastructure.mongo.services;

import com.jeferro.products.reviews.reviews.domain.models.ReviewFilter;
import com.jeferro.products.reviews.reviews.domain.models.ReviewOrder;
import com.jeferro.shared.ddd.infrastructure.mongo.services.QueryMongoCreator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewQueryMongoCreator extends QueryMongoCreator<ReviewOrder, ReviewFilter> {

    @Override
    protected List<Criteria> mapFilter(ReviewFilter filter) {
        var criteria = new ArrayList<Criteria>();

        if (filter.hasDomain()) {
            var domainCriteria = Criteria.where("entityId.domain").is(filter.getDomain());

            criteria.add(domainCriteria);
        }

        if (filter.hasId()) {
            var domainCriteria = Criteria.where("entityId.id").is(filter.getDomain());

            criteria.add(domainCriteria);
        }

        return criteria;
    }

    @Override
    protected String mapOrder(ReviewOrder order) {
        if (order == null) {
            return "name";
        }

        return switch (order) {
            case ID -> "_id";
        };
    }
}
