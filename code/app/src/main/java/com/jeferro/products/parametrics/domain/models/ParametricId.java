package com.jeferro.products.parametrics.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;

public class ParametricId extends StringIdentifier {

  public ParametricId(String value) {
    super(value);
  }

  public static ParametricId createOf(String value) {
    return new ParametricId(value);
  }
}
