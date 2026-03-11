package com.jeferro.shared.ddd.domain.exceptions;

import lombok.Getter;

@Getter
public non-sealed class InternalException extends ApplicationException {

  private final Throwable cause;

  protected InternalException(String code, String message, Throwable cause) {
    super(code, "Internal Error", message);

    this.cause = cause;
  }

  public static InternalException createOf(Throwable cause) {
    return new InternalException(INTERNAL_ERROR_CODE, cause.getMessage(), cause);
  }
}
