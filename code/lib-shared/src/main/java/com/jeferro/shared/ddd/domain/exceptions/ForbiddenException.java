package com.jeferro.shared.ddd.domain.exceptions;

import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;

public non-sealed class ForbiddenException extends ApplicationException {

  protected ForbiddenException(String code, String message) {
    super(code, "Forbidden", message);
  }

  public static ForbiddenException createOf(Auth auth, Set<String> mandatoryRoles) {
    return new ForbiddenException(
        FORBIDDEN_CODE,
        "Auth "
            + auth
            + " has not permission to execute use case. Mandatory roles: "
            + mandatoryRoles);
  }

  public static ForbiddenException createOfNotUserAuth(Auth auth) {
    return new ForbiddenException(FORBIDDEN_CODE, "Auth isn't a user auth: " + auth);
  }
}
