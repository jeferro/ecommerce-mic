package com.jeferro.products.products.products.infrastructure.mongo.services;

import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.products.products.domain.models.filter.ProductFilterOrder;
import com.jeferro.shared.ddd.infrastructure.mongo.services.QueryMongoCreator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryMongoCreator extends QueryMongoCreator<ProductFilterOrder, ProductFilter> {

    @Override
    protected List<Criteria> mapFilter(ProductFilter filter) {
        var criteria = new ArrayList<Criteria>();

        if (filter.hasName()) {
            Criteria nameCriteria = Criteria.where("name")
                    .regex(filter.getName(), "i");

            criteria.add(nameCriteria);
        }

        if (filter.hasCode()) {
            Criteria codeCriteria = Criteria.where("code").is(filter.getCode().getValue());

            criteria.add(codeCriteria);
        }

        if (filter.hasMinEffectiveDate()) {
            Criteria codeCriteria = Criteria.where("effectiveDate").gt(filter.getMinEffectiveDate());

            criteria.add(codeCriteria);
        }

        return criteria;
    }

    @Override
    protected String mapOrder(ProductFilterOrder order) {
        if (order == null) {
            return "name";
        }

        return switch (order) {
            case TYPE_ID -> "typeId";
            case NAME -> "name";
            case START_EFFECTIVE_DATE -> "effectiveDate";
        };
    }
}
