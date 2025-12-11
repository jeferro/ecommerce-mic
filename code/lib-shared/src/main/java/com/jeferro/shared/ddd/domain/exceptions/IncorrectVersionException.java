package com.jeferro.shared.ddd.domain.exceptions;

public class IncorrectVersionException extends ValueValidationException {

  protected IncorrectVersionException(String code, String message) {
    super(code, message);
  }

  public static IncorrectVersionException createOfIncorrectVersion() {
    return new IncorrectVersionException(INCORRECT_VERSION_CODE, "Version of entity to update is incorrect");
  }
}
