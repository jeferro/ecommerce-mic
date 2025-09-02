package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewCriteria;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class SearchReviewParams extends Params<PaginatedList<Review>> {

    private final ReviewCriteria reviewCriteria;

    public SearchReviewParams(ReviewCriteria reviewCriteria) {
        super();

        ValueValidator.isNotNull(reviewCriteria, "reviewCriteria");

        this.reviewCriteria = reviewCriteria;
    }

}
