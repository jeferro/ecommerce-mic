package com.jeferro.ecommerce.support.parametrics.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;

public class ProductTypeMother {

  public static ParametricValueId fruitId() {
    return ParametricValueId.createOf("fruit");
  }
}
