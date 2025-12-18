package com.jeferro.ecommerce.products.product_versions.domain.models.criteria;

import static com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionOrder.NAME;
import static com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionOrder.START_EFFECTIVE_DATE;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCode;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersionCriteria extends DomainCriteria<ProductVersionOrder> {

  private final ProductCode code;

  private final Instant minEffectiveDate;

  private final Instant maxEffectiveDate;

  private final Instant searchDate;

  private final Instant startDate;

  private final Instant endDate;

  public ProductVersionCriteria(
      Integer pageNumber,
      Integer pageSize,
      ProductVersionOrder order,
      Boolean ascending,
      ProductCode code,
      Instant minEffectiveDate,
      Instant maxEffectiveDate,
      Instant searchDate,
      Instant startDate,
      Instant endDate) {
    super(pageNumber, pageSize, order, ascending);

    this.code = code;
    this.minEffectiveDate = minEffectiveDate;
    this.maxEffectiveDate = maxEffectiveDate;
    this.searchDate = searchDate;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public static ProductVersionCriteria allPage() {
    return new ProductVersionCriteria(0, null, NAME, null, null, null, null, null, null, null);
  }

  public static ProductVersionCriteria byCodePage(ProductCode code) {
    return new ProductVersionCriteria(
        0, null, START_EFFECTIVE_DATE, false, code, null, null, null, null, null);
  }

  public static ProductVersionCriteria previousProductVersion(ProductVersionId versionId) {
    return new ProductVersionCriteria(
        0,
        1,
        START_EFFECTIVE_DATE,
        false,
        versionId.getCode(),
        null,
        versionId.getEffectiveDate(),
        null,
        null,
        null);
  }

  public static ProductVersionCriteria nextProductVersion(ProductVersionId versionId) {
    return new ProductVersionCriteria(
        0,
        1,
        START_EFFECTIVE_DATE,
        true,
        versionId.getCode(),
        versionId.getEffectiveDate(),
        null,
        null,
        null,
        null);
  }

  public static ProductVersionCriteria overlappingDateRange(ProductCode code, Instant startDate, Instant endDate) {
    return new ProductVersionCriteria(
        0,
        null,
        START_EFFECTIVE_DATE,
        true,
        code,
        null,
        null,
        null,
        startDate,
        endDate);
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

  public boolean hasStartDate() {
    return startDate != null;
  }

  public boolean hasEndDate() {
    return endDate != null;
  }
}
