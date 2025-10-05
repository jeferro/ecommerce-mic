package com.jeferro.products.products.domain.models.criteria;

import static com.jeferro.products.products.domain.models.criteria.ProductVersionOrder.NAME;
import static com.jeferro.products.products.domain.models.criteria.ProductVersionOrder.START_EFFECTIVE_DATE;

import com.jeferro.products.products.domain.models.ProductCode;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersionCriteria extends DomainCriteria<ProductVersionOrder> {

  private final ProductCode code;

  private final Instant minEffectiveDate;

  private final Instant maxEffectiveDate;

  private final Instant searchDate;

  public ProductVersionCriteria(
      int pageNumber,
      int pageSize,
      ProductVersionOrder order,
      Boolean ascending,
      ProductCode code,
      Instant minEffectiveDate,
      Instant maxEffectiveDate,
      Instant searchDate) {
    super(pageNumber, pageSize, order, ascending);

    this.code = code;
    this.minEffectiveDate = minEffectiveDate;
    this.maxEffectiveDate = maxEffectiveDate;
    this.searchDate = searchDate;
  }

  public static ProductVersionCriteria createEmpty() {
    return new ProductVersionCriteria(0, DEFAULT_PAGE_SIZE, NAME, null, null, null, null, null);
  }

  public static ProductVersionCriteria byCode(ProductCode code) {
    return new ProductVersionCriteria(
        0, DEFAULT_PAGE_SIZE, START_EFFECTIVE_DATE, false, code, null, null, null);
  }

  public static ProductVersionCriteria previousProduct(ProductVersionId versionId) {
    return new ProductVersionCriteria(
        0,
        1,
        START_EFFECTIVE_DATE,
        false,
        versionId.getCode(),
        null,
        versionId.getEffectiveDate(),
        null);
  }

  public static ProductVersionCriteria nextProduct(ProductVersionId versionId) {
    return new ProductVersionCriteria(
        0,
        1,
        START_EFFECTIVE_DATE,
        true,
        versionId.getCode(),
        versionId.getEffectiveDate(),
        null,
        null);
  }

  public boolean hasCode() {
    return code != null;
  }

  public boolean hasMaxEffectiveDate() {
    return maxEffectiveDate != null;
  }

  public boolean hasMinEffectiveDate() {
    return minEffectiveDate != null;
  }

  public boolean hasSearchDate() {
    return searchDate != null;
  }
}
