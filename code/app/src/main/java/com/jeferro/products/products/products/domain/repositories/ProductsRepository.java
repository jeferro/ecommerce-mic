package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.Optional;

public interface ProductsRepository {

    void save(Product product);

    Optional<Product> findById(ProductId id);

    default Product findByIdOrError(ProductId id) {
        return findById(id)
                .orElseThrow(() -> ProductVersionNotFoundException.createOf(id));
    }

    void deleteById(ProductId id);

    PaginatedList<Product> findAll(ProductFilter filter);
}
