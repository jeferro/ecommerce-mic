package com.jeferro.ecommerce.support.parametrics.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PARAMETRIC_VALUE_NOT_FOUND;

import com.jeferro.ecommerce.support.parametrics.domain.models.Parametric;
import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ParametricValueNotFoundException extends NotFoundException {

  private ParametricValueNotFoundException(String message) {
    super(PARAMETRIC_VALUE_NOT_FOUND, "Parametric value not found", message);
  }

  public static ParametricValueNotFoundException createOf(
      Parametric parametric, ParametricValueId missingParametricValueId) {
    return new ParametricValueNotFoundException(
        "Value '" + missingParametricValueId + "' not found in parametric " + parametric.getId());
  }
}
