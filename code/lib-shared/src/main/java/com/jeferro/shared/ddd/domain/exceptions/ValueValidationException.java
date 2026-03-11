package com.jeferro.shared.ddd.domain.exceptions;

public non-sealed class ValueValidationException extends ApplicationException {

  private ValueValidationException(String code, String message) {
    this(code, "Value validation error", message);
  }

  protected ValueValidationException(String code, String title, String message) {
    super(code, title, message);
  }

  public static ValueValidationException createOfMessage(String message) {
    return new ValueValidationException(VALUE_VALIDATION_CODE, message);
  }
}
