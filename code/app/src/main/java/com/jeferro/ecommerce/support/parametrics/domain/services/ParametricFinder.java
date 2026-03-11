package com.jeferro.ecommerce.support.parametrics.domain.services;

import com.jeferro.ecommerce.support.parametrics.domain.exceptions.ParametricNotFoundException;
import com.jeferro.ecommerce.support.parametrics.domain.models.Parametric;
import com.jeferro.ecommerce.support.parametrics.domain.models.ParametricId;
import java.util.Optional;

public interface ParametricFinder {

  Optional<Parametric> findById(ParametricId parametricId);

  default Parametric findByIdOrError(ParametricId parametricId) {
    return findById(parametricId)
        .orElseThrow(() -> ParametricNotFoundException.createOf(parametricId));
  }
}
