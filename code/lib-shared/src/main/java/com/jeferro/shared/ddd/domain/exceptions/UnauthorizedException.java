package com.jeferro.shared.ddd.domain.exceptions;

public final class UnauthorizedException extends ApplicationException {

  private UnauthorizedException(String code, String title, String message) {
    super(code, title, message);
  }

  public static UnauthorizedException createOf() {
    return new UnauthorizedException(UNAUTHORIZED_CODE, "Unauthorized", "Unauthorized");
  }
}
