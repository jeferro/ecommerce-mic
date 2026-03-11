package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductVersionParams extends Params<ProductVersion> {

  private final ProductVersionId productVersionId;

  private final LocalizedField name;

  private final BigDecimal price;

  private final BigDecimal discount;

  private final long version;

  public UpdateProductVersionParams(ProductVersionId productVersionId, LocalizedField name, BigDecimal price, BigDecimal discount, long version) {
    super();

    ValueValidator.ensureNotNull(productVersionId, "productVersionId");

    this.productVersionId = productVersionId;
    this.name = name;
    this.price = price;
    this.discount = discount;
    this.version = version;
  }
}
