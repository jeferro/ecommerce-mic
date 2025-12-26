package com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.MedicalCenterNotFoundException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterCriteria;

import java.util.List;
import java.util.Optional;

public interface MedicalCenterRepository {

  MedicalCenter save(MedicalCenter medicalCenter);

  Optional<MedicalCenter> findById(MedicalCenterId medicalCenterId);

  default MedicalCenter findByIdOrError(MedicalCenterId medicalCenterId) {
    return findById(medicalCenterId)
        .orElseThrow(() -> MedicalCenterNotFoundException.createOf(medicalCenterId));
  }

  void delete(MedicalCenter medicalCenter);

  List<MedicalCenter> findAll(MedicalCenterCriteria criteria);

  default Optional<MedicalCenter> findOne(MedicalCenterCriteria criteria) {
    var result = findAll(criteria);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(result.getFirst());
  }

  long count(MedicalCenterCriteria criteria);
}

