package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
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
