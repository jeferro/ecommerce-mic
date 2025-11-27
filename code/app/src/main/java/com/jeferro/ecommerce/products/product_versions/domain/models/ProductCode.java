package com.jeferro.ecommerce.products.product_versions.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;

public class ProductCode extends StringIdentifier {

  public ProductCode(String value) {
    super(value);
  }
}
