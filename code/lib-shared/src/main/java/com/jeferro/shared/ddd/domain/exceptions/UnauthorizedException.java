package com.jeferro.shared.ddd.domain.exceptions;

public final class UnauthorizedException extends ApplicationException {

    private UnauthorizedException(String message) {
        super(UNAUTHORIZED_CODE, "Unauthorized", message);
    }

    public static UnauthorizedException createOf() {
        return new UnauthorizedException("Unauthorized");
    }
}
