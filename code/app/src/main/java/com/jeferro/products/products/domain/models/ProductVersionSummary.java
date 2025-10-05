package com.jeferro.products.products.domain.models;

import com.jeferro.products.products.domain.models.status.ProductStatus;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersionSummary extends AggregateRoot<ProductVersionId> {

  protected LocalizedField name;

  protected ProductStatus status;

  public ProductVersionSummary(ProductVersionId id, LocalizedField name, ProductStatus status, Metadata metadata) {
    super(id, metadata);

    this.name = name;
    this.status = status;
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
}
