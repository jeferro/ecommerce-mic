package com.jeferro.ecommerce.products.product_versions.domain.repositories;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.shared.domain.repositories.InMemoryRepository;
import java.time.Instant;
import java.util.List;

public class ProductVersionInMemoryRepository
    extends InMemoryRepository<ProductVersion, ProductVersionId>
    implements ProductVersionRepository {

  public ProductVersionInMemoryRepository() {
    var appleV1 = ProductVersionMother.appleV1();
    data.put(appleV1.getId(), appleV1);

    var appleV2 = ProductVersionMother.appleV2();
    data.put(appleV2.getId(), appleV2);

    var pearV1 = ProductVersionMother.pearV1();
    data.put(pearV1.getId(), pearV1);
  }

  @Override
  public List<ProductVersion> findAll(ProductVersionCriteria criteria) {
    var entities =
        data.values().stream()
            .filter(product -> matchCriteria(criteria, product))
            .sorted((p1, p2) -> compareProducts(p1, p2, criteria))
            .toList();

    return paginateEntities(entities, criteria);
  }

  @Override
  public long count(ProductVersionCriteria criteria) {
    return findAll(criteria).size();
  }

  @Override
  public List<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria) {
    return findAll(criteria).stream().map(this::mapProductVersionSummary).toList();
  }

  private ProductVersionSummary mapProductVersionSummary(ProductVersion productVersion) {
    return new ProductVersionSummary(productVersion.getId(), productVersion.getName(),
            productVersion.getPrice(),
            productVersion.getDiscount(),
            productVersion.getTotalPrice(),
            productVersion.getStatus(),
            1L,
            null);
  }

  private boolean matchCriteria(ProductVersionCriteria filter, ProductVersion productVersion) {
    return matchProductCode(productVersion, filter)
        && matchMinEffectiveProductCode(productVersion, filter)
        && matchMaxEffectiveProductCode(productVersion, filter)
        && matchSearchDate(productVersion, filter)
        && matchOverlappingDateRange(productVersion, filter);
  }

  private boolean matchProductCode(ProductVersion productVersion, ProductVersionCriteria filter) {
    return !filter.hasCode() || productVersion.getCode().equals(filter.getCode());
  }

  private boolean matchMinEffectiveProductCode(
      ProductVersion productVersion, ProductVersionCriteria filter) {
    return !filter.hasMinEffectiveDate()
        || productVersion.getEffectiveDate().isAfter(filter.getMinEffectiveDate());
  }

  private boolean matchMaxEffectiveProductCode(
      ProductVersion productVersion, ProductVersionCriteria filter) {
    return !filter.hasMaxEffectiveDate()
        || productVersion.getEffectiveDate().isBefore(filter.getMaxEffectiveDate());
  }

  private boolean matchSearchDate(ProductVersion productVersion, ProductVersionCriteria filter) {
    if (!filter.hasSearchDate()) {
      return true;
    }

    Instant searchDate = filter.getSearchDate();
    Instant effectiveDate = productVersion.getEffectiveDate();
    Instant endEffectiveDate = productVersion.getEndEffectiveDate();

    return (effectiveDate.isBefore(searchDate) || effectiveDate.equals(searchDate))
        && (endEffectiveDate == null
            || endEffectiveDate.isAfter(searchDate)
            || endEffectiveDate.equals(searchDate));
  }

  private boolean matchOverlappingDateRange(ProductVersion productVersion, ProductVersionCriteria filter) {
    if (!filter.hasStartDate() || !filter.hasEndDate()) {
      return true;
    }

    Instant startDate = filter.getStartDate();
    Instant endDate = filter.getEndDate();
    Instant effectiveDate = productVersion.getEffectiveDate();
    Instant endEffectiveDate = productVersion.getEndEffectiveDate();

    // Una versión se solapa con el rango [startDate, endDate) si:
    // effectiveDate < endDate Y (endEffectiveDate == null OR endEffectiveDate > startDate)
    return effectiveDate.isBefore(endDate)
        && (endEffectiveDate == null || endEffectiveDate.isAfter(startDate));
  }

  private int compareProducts(ProductVersion p1, ProductVersion p2, ProductVersionCriteria criteria) {
    var order = criteria.getOrder();

    if(order == null){
      return -1;
    }

    return switch (order) {
      case ID, NAME -> -1;
      case START_EFFECTIVE_DATE -> p2.getEffectiveDate().isAfter(p1.getEffectiveDate()) ? -1 : 1;
    };
  }
}
