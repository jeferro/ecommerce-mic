package com.jeferro.ecommerce.products.product_versions.application.params;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class SearchProductVersionsParams extends Params<PaginatedList<ProductVersionSummary>> {

  private final ProductVersionCriteria criteria;

  public SearchProductVersionsParams(ProductVersionCriteria criteria) {
    super();

    ValueValidator.ensureNotNull(criteria, "criteria");

    this.criteria = criteria;
  }
}
