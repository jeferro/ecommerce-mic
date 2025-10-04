package com.jeferro.products.reviews.application.params;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class SearchReviewParams extends Params<PaginatedList<Review>> {

  private final ReviewCriteria criteria;

  public SearchReviewParams(ReviewCriteria criteria) {
    super();

    ValueValidator.isNotNull(criteria, "criteria");

    this.criteria = criteria;
  }
}
