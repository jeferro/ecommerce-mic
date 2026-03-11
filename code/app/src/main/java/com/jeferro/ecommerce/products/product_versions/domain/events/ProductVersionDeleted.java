package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import lombok.Getter;

@Getter
public class ProductVersionDeleted extends ProductVersionEvent {

  public ProductVersionDeleted(ProductVersion entity) {
    super(entity);
  }

  public static ProductVersionDeleted create(ProductVersion productVersion) {
    return new ProductVersionDeleted(productVersion);
  }
}
