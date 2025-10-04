package com.jeferro.shared.ddd.domain.exceptions;

import lombok.Getter;

@Getter
public non-sealed class InternalException extends ApplicationException {

  private final Exception cause;

  protected InternalException(String code, String title, String message, Exception cause) {
    super(code, title, message);

    this.cause = cause;
  }

  public static InternalException createOf(Exception cause) {
    return new InternalException(INTERNAL_ERROR_CODE, "Internal Error", cause.getMessage(), cause);
  }
}
