package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import lombok.Getter;

@Getter
public class ProductVersionUpdated extends ProductVersionEvent {

  public ProductVersionUpdated(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionUpdated create(ProductVersion productVersion) {
    return new ProductVersionUpdated(productVersion);
  }
}
