package com.jeferro.products.products.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.jeferro.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;

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
