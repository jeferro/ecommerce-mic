package com.jeferro.ecommerce.products.product_versions.domain.models;

import com.jeferro.ecommerce.products.product_versions.domain.models.status.ProductStatus;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersionSummary extends AggregateRoot<ProductVersionId> {

  protected final LocalizedField name;

  protected final ProductStatus status;

  private final BigDecimal prize;

  private final BigDecimal discount;

  public ProductVersionSummary(ProductVersionId id,
                               LocalizedField name,
                               BigDecimal prize,
                               BigDecimal discount,
                               ProductStatus status,
                               long version,
                               Metadata metadata) {
    super(id, version, metadata);

    this.name = name;
    this.status = status;
    this.prize = prize;
    this.discount = discount;
  }

  @Override
  @Deprecated
  public ProductVersionId getId() {
    return id;
  }

  public ProductVersionId getVersionId() {
    return id;
  }

  public ProductCode getCode() {
    return id.getCode();
  }

  public Instant getEffectiveDate() {
    return id.getEffectiveDate();
  }

  public BigDecimal getTotalPrice() {
    return prize.multiply(discount);
  }
}
