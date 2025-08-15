package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

public class ProductsInMemoryRepository extends InMemoryRepository<Product, ProductId>
        implements ProductsRepository {

  @Override
    public PaginatedList<Product> findAll(ProductFilter filter) {
        var entities = data.values().stream()
                .filter(product -> matchProduct(filter, product))
                .toList();

        var paginatedEntities = paginateEntities(entities, filter);

        return PaginatedList.createOfList(paginatedEntities);
    }

    private boolean matchProduct(ProductFilter filter, Product product) {
        return matchProductName(filter, product);
    }

    private boolean matchProductName(ProductFilter filter, Product product) {
        return !filter.hasName()
                || product.getName().containsValue(filter.getName());
    }
}
