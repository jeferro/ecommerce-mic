package com.jeferro.products.reviews.reviews.application.params;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class SearchProductReviewParams extends Params<PaginatedList<ProductReview>> {

    private final ProductCode productCode;

    public SearchProductReviewParams(ProductCode productCode) {
        super();

        ValueValidationUtils.isNotNull(productCode, "productCode", this);

        this.productCode = productCode;
    }

}
