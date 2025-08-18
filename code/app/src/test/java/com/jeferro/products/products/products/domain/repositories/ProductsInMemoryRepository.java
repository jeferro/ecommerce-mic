package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

public class ProductsInMemoryRepository extends InMemoryRepository<Product, ProductId>
	implements ProductsRepository {

  public ProductsInMemoryRepository() {
	var appleV1 = ProductMother.appleV1();
	data.put(appleV1.getId(), appleV1);

	var appleV2 = ProductMother.appleV2();
	data.put(appleV2.getId(), appleV2);

	var pearV1 = ProductMother.pearV1();
	data.put(pearV1.getId(), pearV1);
  }

  @Override
  public PaginatedList<Product> findAll(ProductFilter filter) {
	var entities = data.values().stream()
		.filter(product -> matchProduct(filter, product))
		.sorted((p1, p2) -> compareProducts(p1, p2, filter))
		.toList();

	var paginatedEntities = paginateEntities(entities, filter);

	return PaginatedList.createOfList(paginatedEntities);
  }

  private boolean matchProduct(ProductFilter filter, Product product) {
	return matchProductName(product, filter)
		&& matchProductCode(product, filter)
		&& matchMinEffectiveProductCode(product, filter)
		&& matchMaxEffectiveProductCode(product, filter);
  }

  private boolean matchProductName(Product product, ProductFilter filter) {
	return !filter.hasName()
		|| product.getName().containsValue(filter.getName());
  }

  private boolean matchProductCode(Product product, ProductFilter filter) {
	return !filter.hasCode()
		|| product.getCode().equals(filter.getCode());
  }

  private boolean matchMinEffectiveProductCode(Product product, ProductFilter filter) {
	return !filter.hasMinEffectiveDate()
		|| product.getEffectiveDate().isAfter(filter.getMinEffectiveDate());
  }

  private boolean matchMaxEffectiveProductCode(Product product, ProductFilter filter) {
	return !filter.hasMaxEffectiveDate()
		|| product.getEffectiveDate().isBefore(filter.getMaxEffectiveDate());
  }

  private int compareProducts(Product p1, Product p2, ProductFilter filter) {
	return switch (filter.getOrder()) {
	  case NAME, TYPE_ID -> -1;
	  case START_EFFECTIVE_DATE -> p2.getEffectiveDate().isAfter(p1.getEffectiveDate()) ? -1 : 1;
	};
  }
}
