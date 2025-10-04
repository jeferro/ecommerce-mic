package com.jeferro.products.parametrics.domain.models;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;

public class ProductTypeMother {

  public static ParametricValueId fruitId() {
    return ParametricValueId.createOf("fruit");
  }
}
