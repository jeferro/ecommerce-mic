package com.jeferro.ecommerce.support.parametrics.domain.models.values;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;

public class ParametricValueId extends StringIdentifier {

  public ParametricValueId(String value) {
    super(value);
  }

  public static ParametricValueId createOf(String value) {
    return new ParametricValueId(value);
  }
}
