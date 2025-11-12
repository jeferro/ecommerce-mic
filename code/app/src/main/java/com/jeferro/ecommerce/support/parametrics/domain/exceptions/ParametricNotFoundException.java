package com.jeferro.ecommerce.support.parametrics.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PARAMETRIC_NOT_FOUND;

import com.jeferro.ecommerce.support.parametrics.domain.models.ParametricId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ParametricNotFoundException extends NotFoundException {

  private ParametricNotFoundException(String message) {
    super(PARAMETRIC_NOT_FOUND, "Parametric not found", message);
  }

  public static ParametricNotFoundException createOf(ParametricId parametricId) {
    return new ParametricNotFoundException("Parametric '" + parametricId + "' not found");
  }
}
