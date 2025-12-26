package com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.MedicalCenterExceptionCodes.REGISTRATION_DATE_BEFORE_HOLDER;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
import java.time.LocalDate;

public class RegistrationDateBeforeHolderException extends ValueValidationException {

  protected RegistrationDateBeforeHolderException(String code, String message) {
    super(code, "Registration date before holder registration date", message);
  }

  public static RegistrationDateBeforeHolderException createOf(
      LocalDate registrationDate, LocalDate holderRegistrationDate) {
    return new RegistrationDateBeforeHolderException(
        REGISTRATION_DATE_BEFORE_HOLDER,
        "Medical center registration date '"
            + registrationDate
            + "' cannot be before holder registration date '"
            + holderRegistrationDate
            + "'");
  }
}

