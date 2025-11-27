package com.jeferro.shared.ddd.domain.exceptions;

import lombok.Getter;

@Getter
public non-sealed class InternalException extends ApplicationException {

  private final Throwable cause;

  protected InternalException(String code, String title, String message, Throwable cause) {
    super(code, title, message);

    this.cause = cause;
  }

  public static InternalException createOf(Throwable cause) {
    return new InternalException(INTERNAL_ERROR_CODE, "Internal Error", cause.getMessage(), cause);
  }
}
