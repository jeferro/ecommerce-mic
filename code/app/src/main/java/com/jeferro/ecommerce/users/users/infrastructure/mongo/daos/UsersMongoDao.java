package com.jeferro.ecommerce.users.users.infrastructure.mongo.daos;

import com.jeferro.ecommerce.users.users.domain.models.criteria.UserCriteria;
import com.jeferro.ecommerce.users.users.infrastructure.mongo.dtos.UserMongoDTO;
import com.jeferro.shared.mongo.MongoDao;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class UsersMongoDao extends MongoDao<UserMongoDTO, String, UserCriteria> {

  protected UsersMongoDao(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public Class<UserMongoDTO> getEntityClass() {
    return UserMongoDTO.class;
  }

  @Override
  protected List<Criteria> mapCriteria(UserCriteria domainCriteria) {
    return List.of();
  }

  @Override
  protected String mapOrder(UserCriteria domainCriteria) {
    var order = domainCriteria.getOrder();

    if(order == null){
      return "_id";
    }

    return switch (order) {
      default -> "_id";
    };
  }
}
