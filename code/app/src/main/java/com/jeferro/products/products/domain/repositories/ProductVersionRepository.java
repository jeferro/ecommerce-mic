package com.jeferro.products.products.domain.repositories;

import com.jeferro.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.Optional;

public interface ProductVersionRepository {

    void save(ProductVersion productVersion);

    Optional<ProductVersion> findById(ProductVersionId versionId);

    default ProductVersion findByIdOrError(ProductVersionId versionId) {
        return findById(versionId)
                .orElseThrow(() -> ProductVersionNotFoundException.createOf(versionId));
    }

    void delete(ProductVersion version);

    PaginatedList<ProductVersion> findAll(ProductVersionCriteria filter);

  PaginatedList<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria);
}
