package com.jeferro.products.products.application.params;

import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class SearchProductsParams extends Params<PaginatedList<ProductVersionSummary>> {

  private final ProductVersionCriteria criteria;

  public SearchProductsParams(ProductVersionCriteria criteria) {
    super();

    ValueValidator.isNotNull(criteria, "criteria");

    this.criteria = criteria;
  }
}
