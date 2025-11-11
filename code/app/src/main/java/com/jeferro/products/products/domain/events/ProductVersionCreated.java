package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
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
