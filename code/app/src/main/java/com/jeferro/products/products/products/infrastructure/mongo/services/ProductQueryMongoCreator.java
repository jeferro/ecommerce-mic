package com.jeferro.products.products.products.infrastructure.mongo.services;

import com.jeferro.products.products.products.domain.models.filter.ProductVersionFilter;
import com.jeferro.products.products.products.domain.models.filter.ProductVersionOrder;
import com.jeferro.shared.ddd.infrastructure.mongo.services.QueryMongoCreator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryMongoCreator extends QueryMongoCreator<ProductVersionOrder, ProductVersionFilter> {

    @Override
    protected List<Criteria> mapFilter(ProductVersionFilter filter) {
        var criteria = new ArrayList<Criteria>();

        if (filter.hasCode()) {
            Criteria codeCriteria = Criteria.where("code").is(filter.getCode().getValue());

            criteria.add(codeCriteria);
        }

        if (filter.hasMinEffectiveDate()) {
            Criteria minEffectiveDateCriteria = Criteria.where("effectiveDate").gt(filter.getMinEffectiveDate());

            criteria.add(minEffectiveDateCriteria);
        }

        if (filter.hasMaxEffectiveDate()) {
            Criteria maxEffectiveDateCriteria = Criteria.where("effectiveDate").lt(filter.getMaxEffectiveDate());

            criteria.add(maxEffectiveDateCriteria);
        }

        if (filter.hasSearchDate()) {
            Criteria searchDateCriteria = new Criteria().andOperator(
                Criteria.where("effectiveDate").lte(filter.getSearchDate()),
                new Criteria().orOperator(
                    Criteria.where("endEffectiveDate").is(null),
                    Criteria.where("endEffectiveDate").gte(filter.getSearchDate())
                ));

            criteria.add(searchDateCriteria);
        }

        return criteria;
    }

    @Override
    protected String mapOrder(ProductVersionOrder order) {
        if (order == null) {
            return "name";
        }

        return switch (order) {
            case ID -> "_id";
            case TYPE_ID -> "typeId";
            case NAME -> "name";
            case START_EFFECTIVE_DATE -> "effectiveDate";
        };
    }
}
