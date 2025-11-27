package com.jeferro.shared.ddd.domain.exceptions;

public final class UnauthorizedException extends ApplicationException {

  private UnauthorizedException() {
    super(UNAUTHORIZED_CODE, "Unauthorized", "Unauthorized");
  }

  public static UnauthorizedException createOfUserNotFound() {
    return new UnauthorizedException();
  }

  public static UnauthorizedException createOfWrongPassword() {
    return new UnauthorizedException();
  }
}
