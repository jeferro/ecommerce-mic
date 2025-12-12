package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class PublishProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId productVersionId;

  private final long version;

  public PublishProductVersionParams(ProductVersionId productVersionId, long version) {
    super();

    ValueValidator.ensureIsNotNull(productVersionId, "productVersionId");

    this.productVersionId = productVersionId;
    this.version = version;
  }
}
