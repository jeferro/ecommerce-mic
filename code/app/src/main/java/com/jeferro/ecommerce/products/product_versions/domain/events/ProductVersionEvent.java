package com.jeferro.ecommerce.products.product_versions.domain.events;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ProductVersionEvent extends Event<ProductVersion, ProductVersionId> {

  public ProductVersionEvent(ProductVersion entity) {
    super(entity);
  }
}
