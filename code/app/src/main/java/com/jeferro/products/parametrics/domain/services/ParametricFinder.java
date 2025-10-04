package com.jeferro.products.parametrics.domain.services;

import com.jeferro.products.parametrics.domain.exceptions.ParametricNotFoundException;
import com.jeferro.products.parametrics.domain.models.Parametric;
import com.jeferro.products.parametrics.domain.models.ParametricId;
import java.util.Optional;

public interface ParametricFinder {

  Optional<Parametric> findById(ParametricId parametricId);

  default Parametric findByIdOrError(ParametricId parametricId) {
    return findById(parametricId)
        .orElseThrow(() -> ParametricNotFoundException.createOf(parametricId));
  }
}
