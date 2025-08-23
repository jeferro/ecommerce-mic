package com.jeferro.products.products.products.domain.repositories;

import com.jeferro.products.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.filter.ProductVersionFilter;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.Optional;

public interface ProductVersionRepository {

    void save(ProductVersion productVersion);

    Optional<ProductVersion> findById(ProductVersionId versionId);

    default ProductVersion findByIdOrError(ProductVersionId versionId) {
        return findById(versionId)
                .orElseThrow(() -> ProductVersionNotFoundException.createOf(versionId));
    }

    void deleteById(ProductVersionId versionId);

    PaginatedList<ProductVersion> findAll(ProductVersionFilter filter);
}
