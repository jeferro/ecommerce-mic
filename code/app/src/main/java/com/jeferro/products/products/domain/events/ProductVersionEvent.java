package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;

@Getter
public abstract class ProductVersionEvent extends Event<ProductVersion, ProductVersionId> {

  public ProductVersionEvent(ProductVersion entity) {
    super(entity);
  }
}
