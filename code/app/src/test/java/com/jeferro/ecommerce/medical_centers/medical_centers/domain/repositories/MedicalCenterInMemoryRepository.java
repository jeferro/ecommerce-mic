package com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterCriteria;
import com.jeferro.ecommerce.shared.domain.repositories.InMemoryRepository;
import java.util.List;

public class MedicalCenterInMemoryRepository extends InMemoryRepository<MedicalCenter, MedicalCenterId>
    implements MedicalCenterRepository {

  public MedicalCenterInMemoryRepository() {
  }

  @Override
  public List<MedicalCenter> findAll(MedicalCenterCriteria criteria) {
    var entities =
        data.values().stream()
            .filter(medicalCenter -> matchCriteria(medicalCenter, criteria))
            .sorted((m1, m2) -> compare(m1, m2, criteria))
            .toList();

    return paginateEntities(entities, criteria);
  }

  @Override
  public long count(MedicalCenterCriteria criteria) {
    return findAll(criteria).size();
  }

  private boolean matchCriteria(MedicalCenter medicalCenter, MedicalCenterCriteria criteria) {
    return true;
  }

  private int compare(MedicalCenter m1, MedicalCenter m2, MedicalCenterCriteria criteria) {
    var order = criteria.getOrder();

    if (order == null) {
      return -1;
    }

    return switch (criteria.getOrder()) {
      default -> -1;
    };
  }
}


