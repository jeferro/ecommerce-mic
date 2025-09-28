package com.jeferro.shared.ddd.domain.exceptions;

import lombok.Getter;

@Getter
public non-sealed class InternalError extends ApplicationException {

    private final Exception cause;

    protected InternalError(String code, String title, String message, Exception cause) {
        super(code, title, message);

        this.cause = cause;
    }

    public static InternalError createOf(Exception cause) {
        return new InternalError(INTERNAL_ERROR_CODE, "Internal Error", cause.getMessage(), cause);
    }
}
