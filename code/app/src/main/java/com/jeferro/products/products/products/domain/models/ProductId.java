package com.jeferro.products.products.products.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ProductId extends StringIdentifier {

  private static final String SEPARATOR = "::";

  private final ProductCode code;

  private final Instant startEffectiveDate;

  private ProductId(ProductCode code, Instant startEffectiveDate) {
	super(code + SEPARATOR + startEffectiveDate);

	this.code = code;
	this.startEffectiveDate = startEffectiveDate;
  }

  public ProductId(String value) {
	super(value);

	var split = value.split(SEPARATOR);

	this.code = new ProductCode(split[0]);
	this.startEffectiveDate = Instant.parse(split[1]);
  }

  public static ProductId createOf(ProductCode code, Instant startEffectiveDate) {
	ValueValidationUtils.isNotNull(code, "code", ProductId.class);
	ValueValidationUtils.isNotNull(startEffectiveDate, "startEffectiveDate", ProductId.class);

	return new ProductId(code, startEffectiveDate);
  }
}
