package com.jeferro.ecommerce.products.product_versions.domain.repositories;

import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import java.util.List;
import java.util.Optional;

public interface ProductVersionRepository {

  void save(ProductVersion productVersion);

  Optional<ProductVersion> findById(ProductVersionId versionId);

  default ProductVersion findByIdOrError(ProductVersionId versionId) {
    return findById(versionId)
        .orElseThrow(() -> ProductVersionNotFoundException.createOf(versionId));
  }

  void delete(ProductVersion version);

  List<ProductVersion> findAll(ProductVersionCriteria criteria);

  default Optional<ProductVersion> findOne(ProductVersionCriteria criteria) {
    var result = findAll(criteria);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(result.getFirst());
  }

  long count(ProductVersionCriteria criteria);

  List<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria);
}
