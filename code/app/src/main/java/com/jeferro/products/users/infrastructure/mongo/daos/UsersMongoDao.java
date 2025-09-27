package com.jeferro.products.users.infrastructure.mongo.daos;

import com.jeferro.products.users.domain.models.UserCriteria;
import com.jeferro.products.users.infrastructure.mongo.dtos.UserMongoDTO;
import com.jeferro.shared.ddd.infrastructure.mongo.dao.MongoDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

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
	return switch (domainCriteria.getOrder()){
	  default -> "_id";
	};
  }
}
