package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterCriteria;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories.MedicalCenterRepository;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.daos.MedicalCenterMongoDao;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.mappers.MedicalCenterMongoMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalCenterMongoRepository implements MedicalCenterRepository {

  private final MedicalCenterMongoMapper medicalCenterMongoMapper;
  private final MedicalCenterMongoDao medicalCenterMongoDao;

  @Override
  public MedicalCenter save(MedicalCenter medicalCenter) {
    var medicalCenterDto = medicalCenterMongoMapper.toDTO(medicalCenter);

    var resultDto = medicalCenterMongoDao.save(medicalCenterDto);

    return medicalCenterMongoMapper.toDomain(resultDto);
  }

  @Override
  public Optional<MedicalCenter> findById(MedicalCenterId medicalCenterId) {
    var medicalCenterIdDto = medicalCenterMongoMapper.toDTO(medicalCenterId);

    return medicalCenterMongoDao.findById(medicalCenterIdDto).map(medicalCenterMongoMapper::toDomain);
  }

  @Override
  public void delete(MedicalCenter medicalCenter) {
    var medicalCenterDto = medicalCenterMongoMapper.toDTO(medicalCenter);

    medicalCenterMongoDao.delete(medicalCenterDto);
  }

  @Override
  public List<MedicalCenter> findAll(MedicalCenterCriteria criteria) {
    var page = medicalCenterMongoDao.findAll(criteria);

    return medicalCenterMongoMapper.toDomain(page);
  }

  @Override
  public long count(MedicalCenterCriteria criteria) {
    return medicalCenterMongoDao.count(criteria);
  }
}


