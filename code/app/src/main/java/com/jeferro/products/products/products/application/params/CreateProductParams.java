package com.jeferro.products.products.products.application.params;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class CreateProductParams extends Params<ProductVersion> {

  private final ProductVersionId versionId;

  private final ParametricValueId typeId;

  private final LocalizedField name;

  public CreateProductParams(ProductVersionId versionId,
	  ParametricValueId typeId,
	  LocalizedField name) {
	super();

	ValueValidator.isNotNull(versionId, "versionId");
	ValueValidator.isNotNull(typeId, "typeId");
	ValueValidator.isNotNull(name, "name");

	this.versionId = versionId;
	this.typeId = typeId;
	this.name = name;
  }
}
