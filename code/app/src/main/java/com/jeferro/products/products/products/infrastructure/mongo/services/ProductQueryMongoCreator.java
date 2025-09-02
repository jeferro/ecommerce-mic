package com.jeferro.products.products.products.infrastructure.mongo.services;

import com.jeferro.products.products.products.domain.models.filter.ProductVersionCriteria;
import com.jeferro.products.products.products.domain.models.filter.ProductVersionOrder;
import com.jeferro.shared.ddd.infrastructure.mongo.services.QueryMongoCreator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryMongoCreator extends QueryMongoCreator<ProductVersionOrder, ProductVersionCriteria> {

    @Override
    protected List<Criteria> mapCriteria(ProductVersionCriteria criteria) {
        var mongoCriteria = new ArrayList<Criteria>();

        if (criteria.hasCode()) {
            Criteria codeCriteria = Criteria.where("code").is(criteria.getCode().getValue());

            mongoCriteria.add(codeCriteria);
        }

        if (criteria.hasMinEffectiveDate()) {
            Criteria minEffectiveDateCriteria = Criteria.where("effectiveDate").gt(criteria.getMinEffectiveDate());

            mongoCriteria.add(minEffectiveDateCriteria);
        }

        if (criteria.hasMaxEffectiveDate()) {
            Criteria maxEffectiveDateCriteria = Criteria.where("effectiveDate").lt(criteria.getMaxEffectiveDate());

            mongoCriteria.add(maxEffectiveDateCriteria);
        }

        if (criteria.hasSearchDate()) {
            Criteria searchDateCriteria = new Criteria().andOperator(
                Criteria.where("effectiveDate").lte(criteria.getSearchDate()),
                new Criteria().orOperator(
                    Criteria.where("endEffectiveDate").is(null),
                    Criteria.where("endEffectiveDate").gte(criteria.getSearchDate())
                ));

            mongoCriteria.add(searchDateCriteria);
        }

        return mongoCriteria;
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
