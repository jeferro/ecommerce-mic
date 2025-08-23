package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class SearchProductsParams extends Params<PaginatedList<ProductVersion>> {

    private final ProductFilter filter;

    public SearchProductsParams(ProductFilter filter) {
        super();

        ValueValidationUtils.isNotNull(filter, "filter", this);

        this.filter = filter;
    }
}
