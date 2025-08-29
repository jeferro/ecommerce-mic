package com.jeferro.shared.ddd.domain.exceptions;

import com.jeferro.shared.ddd.domain.models.auth.Auth;

import java.util.Set;

public non-sealed class ForbiddenException extends ApplicationException {

    protected ForbiddenException(String message) {
        super(FORBIDDEN_CODE, "Forbidden", message);
    }

    public static ForbiddenException createOf(Auth auth, Set<String> mandatoryRoles) {
        return new ForbiddenException("Auth " + auth + " has not permission to execute use case. Mandatory roles: " + mandatoryRoles);
    }

    public static ForbiddenException createOfNotUserAuth(Auth auth) {
        return new ForbiddenException("Auth isn't a user auth: " + auth);
    }
}
