package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.rest_api.v1.mappers;

import com.jeferro.ecommerce.medical_centers.medical_centers.application.params.GetMedicalCenterParams;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.rest_api.v1.dtos.MedicalCenterRestDTO;
import com.jeferro.shared.mappers.AggregatePrimaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public abstract class MedicalCenterRestMapper
    extends AggregatePrimaryMapper<MedicalCenter, MedicalCenterId, MedicalCenterRestDTO> {

  public abstract GetMedicalCenterParams toGetMedicalCenterParams(String medicalCenterId);
}


