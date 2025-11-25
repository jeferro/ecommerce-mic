package com.jeferro.shared.ddd.domain.exceptions;

public class IncorrectVersionException extends ValueValidationException {

  protected IncorrectVersionException(String code, String title, String message) {
    super(code, title, message);
  }

  public static IncorrectVersionException createOfIncorrectVersion() {
    return new IncorrectVersionException(INCORRECT_VERSION_CODE, "Incorrect version", "Version of entity to update is incorrect");
  }
}
