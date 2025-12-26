package com.jeferro.ecommerce.medical_centers.medical_centers.application.params;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class GetMedicalCenterParams extends Params<MedicalCenter> {

  private final MedicalCenterId medicalCenterId;

  public GetMedicalCenterParams(MedicalCenterId medicalCenterId) {
    super();

    ValueValidator.ensureNotNull(medicalCenterId, "medicalCenterId");

    this.medicalCenterId = medicalCenterId;
  }
}


