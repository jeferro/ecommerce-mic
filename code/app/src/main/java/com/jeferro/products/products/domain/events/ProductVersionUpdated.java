package com.jeferro.products.products.domain.events;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class ProductVersionUpdated extends ProductVersionEvent {

  private final LocalizedField name;

  private final ProductStatus status;

  private ProductVersionUpdated(
      ProductVersionId versionId, LocalizedField name, ProductStatus status) {
    super(versionId);

    this.name = name;
    this.status = status;
  }

  public static ProductVersionUpdated create(ProductVersion productVersion) {
    var versionId = productVersion.getVersionId();
    var name = productVersion.getName();
    var status = productVersion.getStatus();

    return new ProductVersionUpdated(versionId, name, status);
  }
}
