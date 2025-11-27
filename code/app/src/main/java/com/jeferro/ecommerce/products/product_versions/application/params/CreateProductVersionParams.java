package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class CreateProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId productVersionId;

  private final ParametricValueId typeId;

  private final LocalizedField name;

  public CreateProductVersionParams(ProductVersionId productVersionId, ParametricValueId typeId, LocalizedField name) {
    super();

    ValueValidator.isNotNull(productVersionId, "productVersionId");
    ValueValidator.isNotNull(typeId, "typeId");
    ValueValidator.isNotNull(name, "name");

    this.productVersionId = productVersionId;
    this.typeId = typeId;
    this.name = name;
  }
}
