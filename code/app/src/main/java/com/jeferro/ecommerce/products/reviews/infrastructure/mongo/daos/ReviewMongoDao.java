package com.jeferro.ecommerce.products.reviews.infrastructure.mongo.daos;

import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.infrastructure.mongo.dtos.ReviewMongoDTO;
import com.jeferro.shared.mongo.MongoDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class ReviewMongoDao extends MongoDao<ReviewMongoDTO, String, ReviewCriteria> {

  protected ReviewMongoDao(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public Class<ReviewMongoDTO> getEntityClass() {
    return ReviewMongoDTO.class;
  }

  @Override
  protected List<Criteria> mapCriteria(ReviewCriteria domainCriteria) {
    var mongoCriteria = new ArrayList<Criteria>();

    if (domainCriteria.hasEntityId()) {
      var entityIdCriteria =
          new Criteria()
              .andOperator(
                  Criteria.where("entityId.domain").is(domainCriteria.getEntityId().getDomain()),
                  Criteria.where("entityId.id").is(domainCriteria.getEntityId().getId()));

      mongoCriteria.add(entityIdCriteria);
    }

    return mongoCriteria;
  }

  @Override
  protected String mapOrder(ReviewCriteria domainCriteria) {
    var order = domainCriteria.getOrder();

    if(order == null){
      return "_id";
    }

    return switch (order) {
      default -> "_id";
    };
  }
}
