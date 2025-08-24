package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.products.domain.models.filter.ProductVersionFilter;
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
  public PaginatedList<ProductVersion> findAll(ProductVersionFilter filter) {
	var entities = data.values().stream()
		.filter(product -> matchProduct(filter, product))
		.sorted((p1, p2) -> compareProducts(p1, p2, filter))
		.toList();

	var paginatedEntities = paginateEntities(entities, filter);

	return PaginatedList.createOfList(paginatedEntities);
  }

  private boolean matchProduct(ProductVersionFilter filter, ProductVersion productVersion) {
	return matchProductCode(productVersion, filter)
		&& matchMinEffectiveProductCode(productVersion, filter)
		&& matchMaxEffectiveProductCode(productVersion, filter)
		&& matchSearchDate(productVersion, filter);
  }

  private boolean matchProductCode(ProductVersion productVersion, ProductVersionFilter filter) {
	return !filter.hasCode()
		|| productVersion.getCode().equals(filter.getCode());
  }

  private boolean matchMinEffectiveProductCode(ProductVersion productVersion, ProductVersionFilter filter) {
	return !filter.hasMinEffectiveDate()
		|| productVersion.getEffectiveDate().isAfter(filter.getMinEffectiveDate());
  }

  private boolean matchMaxEffectiveProductCode(ProductVersion productVersion, ProductVersionFilter filter) {
	return !filter.hasMaxEffectiveDate()
		|| productVersion.getEffectiveDate().isBefore(filter.getMaxEffectiveDate());
  }

  private boolean matchSearchDate(ProductVersion productVersion, ProductVersionFilter filter) {
	if(!filter.hasSearchDate()){
	  return true;
	}

	Instant searchDate = filter.getSearchDate();
	Instant effectiveDate = productVersion.getEffectiveDate();
	Instant endEffectiveDate = productVersion.getEndEffectiveDate();

	return (effectiveDate.isBefore(searchDate) || effectiveDate.equals(searchDate))
			&& (endEffectiveDate == null || endEffectiveDate.isAfter(searchDate) || endEffectiveDate.equals(searchDate));
  }

  private int compareProducts(ProductVersion p1, ProductVersion p2, ProductVersionFilter filter) {
	return switch (filter.getOrder()) {
	  case NAME, TYPE_ID -> -1;
	  case START_EFFECTIVE_DATE -> p2.getEffectiveDate().isAfter(p1.getEffectiveDate()) ? -1 : 1;
	};
  }
}
