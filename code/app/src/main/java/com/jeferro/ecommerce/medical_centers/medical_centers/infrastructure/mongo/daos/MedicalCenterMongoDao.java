package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.daos;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterCriteria;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.dtos.MedicalCenterMongoDTO;
import com.jeferro.shared.mongo.MongoDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class MedicalCenterMongoDao extends MongoDao<MedicalCenterMongoDTO, String, MedicalCenterCriteria> {

  protected MedicalCenterMongoDao(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public Class<MedicalCenterMongoDTO> getEntityClass() {
    return MedicalCenterMongoDTO.class;
  }

  @Override
  protected List<Criteria> mapCriteria(MedicalCenterCriteria domainCriteria) {
    return new ArrayList<Criteria>();
  }

  @Override
  protected String mapOrder(MedicalCenterCriteria domainCriteria) {
    var order = domainCriteria.getOrder();

    if (order == null) {
      return "_id";
    }

    return switch (order) {
      default -> "_id";
    };
  }
}


