package com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.MedicalCenterExceptionCodes.MEDICAL_CENTER_NOT_FOUND;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class MedicalCenterNotFoundException extends NotFoundException {

  protected MedicalCenterNotFoundException(String code, String message) {
    super(code, "Medical center not found", message);
  }

  public static MedicalCenterNotFoundException createOf(MedicalCenterId medicalCenterId) {
    return new MedicalCenterNotFoundException(
        MEDICAL_CENTER_NOT_FOUND, "Medical center '" + medicalCenterId + "' not found");
  }
}

