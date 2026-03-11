package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import lombok.Getter;

@Getter
public class ProductVersionCreated extends ProductVersionEvent {

  public ProductVersionCreated(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionCreated create(ProductVersion productVersion) {
    return new ProductVersionCreated(productVersion);
  }
}
