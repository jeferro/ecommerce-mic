package com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.MedicalCenterExceptionCodes.INVALID_ADDRESS_CONTACT_DATA;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

public class InvalidAddressContactDataException extends ValueValidationException {

  protected InvalidAddressContactDataException(String code, String message) {
    super(code, "Invalid address contact data", message);
  }

  public static InvalidAddressContactDataException createOf(String message) {
    return new InvalidAddressContactDataException(INVALID_ADDRESS_CONTACT_DATA, message);
  }
}

