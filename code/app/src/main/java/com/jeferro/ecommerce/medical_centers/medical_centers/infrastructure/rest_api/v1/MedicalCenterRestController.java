package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.rest_api.v1;

import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.rest_api.v1.dtos.MedicalCenterRestDTO;
import com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.rest_api.v1.mappers.MedicalCenterRestMapper;
import com.jeferro.shared.ddd.application.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MedicalCenterRestController implements MedicalCentersApi {

  private final MedicalCenterRestMapper medicalCenterRestMapper;

  private final UseCaseBus useCaseBus;

  @Override
  public MedicalCenterRestDTO getMedicalCenter(String medicalCenterId) {
    var params = medicalCenterRestMapper.toGetMedicalCenterParams(medicalCenterId);

    var medicalCenter = useCaseBus.execute(params);

    return medicalCenterRestMapper.toDTO(medicalCenter);
  }
}


