package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;

public class ProductVersionUnpublished extends ProductVersionEvent {

  public ProductVersionUnpublished(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionUnpublished create(ProductVersion productVersion) {
    return new ProductVersionUnpublished(productVersion);
  }
}
