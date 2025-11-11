package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;

public class ProductVersionPublished extends ProductVersionEvent {

  public ProductVersionPublished(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionPublished create(ProductVersion productVersion) {
    return new ProductVersionPublished(productVersion);
  }
}
