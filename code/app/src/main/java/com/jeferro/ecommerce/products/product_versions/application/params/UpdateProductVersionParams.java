package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class UpdateProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId versionId;

  private final LocalizedField name;

  public UpdateProductVersionParams(ProductVersionId versionId, LocalizedField name) {
    super();

    ValueValidator.isNotNull(versionId, "id");
    ValueValidator.isNotNull(name, "name");

    this.versionId = versionId;
    this.name = name;
  }
}
