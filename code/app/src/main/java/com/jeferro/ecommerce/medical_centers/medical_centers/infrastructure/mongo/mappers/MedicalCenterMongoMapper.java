package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.mappers;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.dtos.MedicalCenterMongoDTO;
import com.jeferro.shared.mappers.AggregateSecondaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public abstract class MedicalCenterMongoMapper
    extends AggregateSecondaryMapper<MedicalCenter, MedicalCenterId, MedicalCenterMongoDTO> {
}


