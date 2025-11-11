package com.jeferro.products.products.infrastructure.mongo.daos;

import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.shared.mongo.MongoDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class ProductVersionsMongoDao
    extends MongoDao<ProductVersionMongoDTO, String, ProductVersionCriteria> {

  protected ProductVersionsMongoDao(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public Class<ProductVersionMongoDTO> getEntityClass() {
    return ProductVersionMongoDTO.class;
  }

  @Override
  protected List<Criteria> mapCriteria(ProductVersionCriteria domainCriteria) {
    var mongoCriteria = new ArrayList<Criteria>();

    if (domainCriteria.hasCode()) {
      Criteria codeCriteria = Criteria.where("code").is(domainCriteria.getCode().toString());

      mongoCriteria.add(codeCriteria);
    }

    if (domainCriteria.hasMinEffectiveDate()) {
      Criteria minEffectiveDateCriteria =
          Criteria.where("effectiveDate").gt(domainCriteria.getMinEffectiveDate());

      mongoCriteria.add(minEffectiveDateCriteria);
    }

    if (domainCriteria.hasMaxEffectiveDate()) {
      Criteria maxEffectiveDateCriteria =
          Criteria.where("effectiveDate").lt(domainCriteria.getMaxEffectiveDate());

      mongoCriteria.add(maxEffectiveDateCriteria);
    }

    if (domainCriteria.hasSearchDate()) {
      Criteria searchDateCriteria =
          new Criteria()
              .andOperator(
                  Criteria.where("effectiveDate").lte(domainCriteria.getSearchDate()),
                  new Criteria()
                      .orOperator(
                          Criteria.where("endEffectiveDate").is(null),
                          Criteria.where("endEffectiveDate").gte(domainCriteria.getSearchDate())));

      mongoCriteria.add(searchDateCriteria);
    }

    return mongoCriteria;
  }

  @Override
  protected String mapOrder(ProductVersionCriteria domainCriteria) {
    return switch (domainCriteria.getOrder()) {
      case NAME -> "name";
      case START_EFFECTIVE_DATE -> "effectiveDate";
      default -> "_id";
    };
  }
}
