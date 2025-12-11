package com.jeferro.shared.ddd.domain.exceptions;

public non-sealed class ValueValidationException extends ApplicationException {

  protected ValueValidationException(String code, String message) {
    super(code, "Value validation error", message);
  }

  public static ValueValidationException createOfMessage(String message) {
    return new ValueValidationException(VALUE_VALIDATION_CODE, message);
  }
}
