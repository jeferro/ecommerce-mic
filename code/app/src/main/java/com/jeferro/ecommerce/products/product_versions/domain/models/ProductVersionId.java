package com.jeferro.ecommerce.products.product_versions.domain.models;

import com.jeferro.ecommerce.products.product_versions.domain.services.InstantTruncator;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersionId extends StringIdentifier {

  private static final String SEPARATOR = "::";

  private final ProductCode code;

  private final Instant effectiveDate;

  public ProductVersionId(String value) {
    super(value);

    var split = value.split(SEPARATOR);

    if (split.length != 2) {
      throw ValueValidationException.createOfMessage("Incorrect format " + value);
    }

    this.code = new ProductCode(split[0]);
    this.effectiveDate = Instant.parse(split[1]);
  }

  private ProductVersionId(ProductCode code, Instant effectiveDate) {
    super(code + SEPARATOR + effectiveDate);

    this.code = code;
    this.effectiveDate = effectiveDate;
  }

  public static ProductVersionId createOf(ProductCode code, Instant effectiveDate) {
    ValueValidator.ensureNotNull(code, "code");
    ValueValidator.ensureNotNull(effectiveDate, "effectiveDate");

    var truncatedEffectiveDate = InstantTruncator.trunkToSeconds(effectiveDate);

    return new ProductVersionId(code, truncatedEffectiveDate);
  }
}
