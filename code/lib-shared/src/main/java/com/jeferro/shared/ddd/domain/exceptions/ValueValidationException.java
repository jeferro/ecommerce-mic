package com.jeferro.shared.ddd.domain.exceptions;

public non-sealed class ValueValidationException extends ApplicationException {

    protected ValueValidationException(String code, String title, String message) {
        super(code, title, message);
    }

    private ValueValidationException(String message) {
        super(VALUE_VALIDATION_CODE, "Value validation error", message);
    }

    public static ValueValidationException createOfMessage(String message) {
        return new ValueValidationException(message);
    }
}
