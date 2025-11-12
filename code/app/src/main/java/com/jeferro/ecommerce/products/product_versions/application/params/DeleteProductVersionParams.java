package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class DeleteProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId versionId;

  public DeleteProductVersionParams(ProductVersionId versionId) {
    super();

    ValueValidator.isNotNull(versionId, "id");

    this.versionId = versionId;
  }
}
