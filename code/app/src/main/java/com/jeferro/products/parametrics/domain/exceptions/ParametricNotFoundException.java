package com.jeferro.products.parametrics.domain.exceptions;

import com.jeferro.products.parametrics.domain.models.ParametricId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.PARAMETRIC_NOT_FOUND;

public class ParametricNotFoundException extends NotFoundException {

    private ParametricNotFoundException(String message) {
        super(PARAMETRIC_NOT_FOUND, "Parametric not found", message);
    }

    public static ParametricNotFoundException createOf(ParametricId parametricId) {
        return new ParametricNotFoundException("Parametric '" + parametricId + "' not found");
    }
}
