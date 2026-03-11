package com.jeferro.ecommerce.support.parametrics.domain.services;

import com.jeferro.ecommerce.support.parametrics.domain.models.ParametricId;
import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParametricValidator {

  private final ParametricFinder parametricFinder;

  public void validateProductType(ParametricValueId productTypeValueId) {
    var productTypeId = ParametricId.createOf("product-types");

    findAndValidateParametricValue(productTypeId, productTypeValueId);
  }

  private void findAndValidateParametricValue(
      ParametricId parametricId, ParametricValueId parametricValueId) {
    var parametric = parametricFinder.findByIdOrError(parametricId);

    parametric.validate(parametricValueId);
  }
}
