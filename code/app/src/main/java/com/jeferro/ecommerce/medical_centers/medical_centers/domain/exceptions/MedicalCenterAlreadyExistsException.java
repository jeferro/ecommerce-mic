package com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.MedicalCenterExceptionCodes.MEDICAL_CENTER_ALREADY_EXISTS;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

public class MedicalCenterAlreadyExistsException extends ValueValidationException {

  protected MedicalCenterAlreadyExistsException(String code, String message) {
    super(code, "Medical center already exists", message);
  }

  public static MedicalCenterAlreadyExistsException createOf(MedicalCenterId medicalCenterId) {
    return new MedicalCenterAlreadyExistsException(
        MEDICAL_CENTER_ALREADY_EXISTS, "Medical center '" + medicalCenterId + "' already exists");
  }
}

