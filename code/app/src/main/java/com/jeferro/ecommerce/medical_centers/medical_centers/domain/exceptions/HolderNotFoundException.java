package com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.MedicalCenterExceptionCodes.HOLDER_NOT_FOUND;

import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class HolderNotFoundException extends NotFoundException {

  protected HolderNotFoundException(String code, String message) {
    super(code, "Holder not found", message);
  }

  public static HolderNotFoundException createOf(String holderId) {
    return new HolderNotFoundException(
        HOLDER_NOT_FOUND, "Holder '" + holderId + "' not found");
  }
}

