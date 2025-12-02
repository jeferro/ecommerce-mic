package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId productVersionId;

  private final ParametricValueId typeId;

  private final LocalizedField name;

  private final BigDecimal price;

  private final BigDecimal discount;

  public CreateProductVersionParams(ProductVersionId productVersionId, ParametricValueId typeId, LocalizedField name, BigDecimal price, BigDecimal discount) {
    super();

    this.productVersionId = productVersionId;
    this.typeId = typeId;
    this.name = name;
    this.price = price;
    this.discount = discount;
  }
}
