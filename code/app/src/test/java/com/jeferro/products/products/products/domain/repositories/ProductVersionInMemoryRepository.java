package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.time.Instant;

public class ProductVersionInMemoryRepository extends InMemoryRepository<ProductVersion, ProductVersionId>
	implements ProductVersionRepository {

  public ProductVersionInMemoryRepository() {
	var appleV1 = ProductVersionMother.appleV1();
	data.put(appleV1.getVersionId(), appleV1);

	var appleV2 = ProductVersionMother.appleV2();
	data.put(appleV2.getVersionId(), appleV2);

	var pearV1 = ProductVersionMother.pearV1();
	data.put(pearV1.getVersionId(), pearV1);
  }

  @Override
  public PaginatedList<ProductVersion> findAll(ProductFilter filter) {
	var entities = data.values().stream()
		.filter(product -> matchProduct(filter, product))
		.sorted((p1, p2) -> compareProducts(p1, p2, filter))
		.toList();

	var paginatedEntities = paginateEntities(entities, filter);

	return PaginatedList.createOfList(paginatedEntities);
  }

  private boolean matchProduct(ProductFilter filter, ProductVersion productVersion) {
	return matchProductName(productVersion, filter)
		&& matchProductCode(productVersion, filter)
		&& matchMinEffectiveProductCode(productVersion, filter)
		&& matchMaxEffectiveProductCode(productVersion, filter)
		&& matchSearchDate(productVersion, filter);
  }

  private boolean matchProductName(ProductVersion productVersion, ProductFilter filter) {
	return !filter.hasName()
		|| productVersion.getName().containsValue(filter.getName());
  }

  private boolean matchProductCode(ProductVersion productVersion, ProductFilter filter) {
	return !filter.hasCode()
		|| productVersion.getCode().equals(filter.getCode());
  }

  private boolean matchMinEffectiveProductCode(ProductVersion productVersion, ProductFilter filter) {
	return !filter.hasMinEffectiveDate()
		|| productVersion.getEffectiveDate().isAfter(filter.getMinEffectiveDate());
  }

  private boolean matchMaxEffectiveProductCode(ProductVersion productVersion, ProductFilter filter) {
	return !filter.hasMaxEffectiveDate()
		|| productVersion.getEffectiveDate().isBefore(filter.getMaxEffectiveDate());
  }

  private boolean matchSearchDate(ProductVersion productVersion, ProductFilter filter) {
	if(!filter.hasSearchDate()){
	  return true;
	}

	Instant searchDate = filter.getSearchDate();
	Instant effectiveDate = productVersion.getEffectiveDate();
	Instant endEffectiveDate = productVersion.getEndEffectiveDate();

	return (effectiveDate.isBefore(searchDate) || effectiveDate.equals(searchDate))
			&& (endEffectiveDate == null || endEffectiveDate.isAfter(searchDate) || endEffectiveDate.equals(searchDate));
  }

  private int compareProducts(ProductVersion p1, ProductVersion p2, ProductFilter filter) {
	return switch (filter.getOrder()) {
	  case NAME, TYPE_ID -> -1;
	  case START_EFFECTIVE_DATE -> p2.getEffectiveDate().isAfter(p1.getEffectiveDate()) ? -1 : 1;
	};
  }
}
