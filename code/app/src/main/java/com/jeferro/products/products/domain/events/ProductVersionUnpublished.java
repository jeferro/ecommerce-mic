package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;

public class ProductVersionUnpublished extends ProductVersionEvent {

  public ProductVersionUnpublished(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionUnpublished create(ProductVersion productVersion) {
    return new ProductVersionUnpublished(productVersion);
  }
}
