package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;

public class ProductVersionPublished extends ProductVersionEvent {

  public ProductVersionPublished(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionPublished create(ProductVersion productVersion) {
    return new ProductVersionPublished(productVersion);
  }
}
