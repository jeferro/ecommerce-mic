package com.jeferro.products.products.application.params;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class GetProductParams extends Params<ProductVersion> {

  private final ProductVersionId versionId;

  public GetProductParams(ProductVersionId versionId) {
    super();

    ValueValidator.isNotNull(versionId, "id");

    this.versionId = versionId;
  }
}
