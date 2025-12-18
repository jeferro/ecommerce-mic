package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCode;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import java.time.Instant;
import lombok.Getter;

@Getter
public class DeleteProductVersionInPeriodParams extends Params<Void> {

  private final ProductCode productCode;

  private final Instant startDate;

  private final Instant endDate;

  public DeleteProductVersionInPeriodParams(ProductCode productCode, Instant startDate, Instant endDate) {
    super();

    ValueValidator.ensureNotNull(productCode, "productCode");
    ValueValidator.ensureNotNull(startDate, "startDate");
    ValueValidator.ensureNotNull(endDate, "endDate");
    ValueValidator.ensure(
        () -> endDate.isAfter(startDate),
        "endDate must be after startDate");

    this.productCode = productCode;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}

