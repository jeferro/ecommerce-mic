package com.jeferro.products.products.products.application.params;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class CreateProductParams extends Params<Product> {

  private final ProductId id;

  private final ParametricValueId typeId;

  private final LocalizedField name;

  public CreateProductParams(ProductId id,
	  ParametricValueId typeId,
	  LocalizedField name) {
	super();

	ValueValidationUtils.isNotNull(id, "id", this);
	ValueValidationUtils.isNotNull(typeId, "typeId", this);
	ValueValidationUtils.isNotNull(name, "name", this);

	this.id = id;
	this.typeId = typeId;
	this.name = name;
  }
}
