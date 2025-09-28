package com.jeferro.shared.ddd.domain.exceptions;

import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public sealed abstract class ApplicationException extends RuntimeException
	permits ForbiddenException, NotFoundException, UnauthorizedException, ValueValidationException, InternalException {

    protected static final String VALUE_VALIDATION_CODE = "value-validation";

    protected static final String FORBIDDEN_CODE = "forbidden";

    protected static final String INTERNAL_ERROR_CODE = "internal";

    protected static final String UNAUTHORIZED_CODE = "unauthorized";

    private final String code;

    private final String title;

    protected ApplicationException(String code, String title, String message) {
        super(message);

        ValueValidator.isNotBlank(code, "code");
        ValueValidator.isNotBlank(title, "title");

        this.code = code;
        this.title = title;
    }
}
