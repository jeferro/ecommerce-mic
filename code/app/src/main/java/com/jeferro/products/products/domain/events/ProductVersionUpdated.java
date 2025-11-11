package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
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
