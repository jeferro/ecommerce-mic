package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.products.products.domain.services.InstantTruncator;
import com.jeferro.products.shared.domain.utils.InstantUtils;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ProductVersionId extends StringIdentifier {

  private static final String SEPARATOR = "::";

  private static final String PATTERN = "yyyy-MM-dd-HH-mm-ss";

  private final ProductCode code;

  private final Instant effectiveDate;

  private ProductVersionId(ProductCode code, Instant effectiveDate) {
	super(code + SEPARATOR + InstantUtils.toString(effectiveDate, PATTERN));

	this.code = code;
	this.effectiveDate = effectiveDate;
  }

  public ProductVersionId(String value) {
	super(value);

	var split = value.split(SEPARATOR);

	this.code = new ProductCode(split[0]);
	this.effectiveDate = InstantUtils.parse(split[1], PATTERN);
  }

  public static ProductVersionId createOf(ProductCode code, Instant effectiveDate) {
	ValueValidationUtils.isNotNull(code, "code", ProductVersionId.class);
	ValueValidationUtils.isNotNull(effectiveDate, "effectiveDate", ProductVersionId.class);

	var truncatedEffectiveDate = InstantTruncator.trunkToSeconds(effectiveDate);

	return new ProductVersionId(code, truncatedEffectiveDate);
  }
}
