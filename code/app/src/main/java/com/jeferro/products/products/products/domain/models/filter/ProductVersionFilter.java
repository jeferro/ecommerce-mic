package com.jeferro.products.products.products.domain.models.filter;

import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.models.filter.Filter;
import lombok.Getter;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.filter.ProductVersionOrder.NAME;
import static com.jeferro.products.products.products.domain.models.filter.ProductVersionOrder.START_EFFECTIVE_DATE;

@Getter
public class ProductVersionFilter extends Filter<ProductVersionOrder> {

    private final ProductCode code;

    private final Instant minEffectiveDate;

    private final Instant maxEffectiveDate;

    private final Instant searchDate;

    public ProductVersionFilter(int pageNumber,
        int pageSize,
        ProductVersionOrder order,
        Boolean ascending,
        ProductCode code,
        Instant minEffectiveDate,
        Instant maxEffectiveDate,
        Instant searchDate) {
        super(pageNumber, pageSize, order, ascending);

        this.code = code;
        this.minEffectiveDate = minEffectiveDate;
        this.maxEffectiveDate = maxEffectiveDate;
	    this.searchDate = searchDate;
	}

    public static ProductVersionFilter createEmpty() {
        return new ProductVersionFilter(0, DEFAULT_PAGE_SIZE, NAME, null, null, null, null, null);
    }

    public static ProductVersionFilter byCode(ProductCode code) {
        return new ProductVersionFilter(0, DEFAULT_PAGE_SIZE, START_EFFECTIVE_DATE, false, code, null, null, null);
    }

    public static ProductVersionFilter previousProduct(ProductVersionId versionId) {
        return new ProductVersionFilter(0, 1, START_EFFECTIVE_DATE, false, versionId.getCode(), null, versionId.getEffectiveDate(), null);
    }

    public static ProductVersionFilter nextProduct(ProductVersionId versionId) {
        return new ProductVersionFilter(0, 1, START_EFFECTIVE_DATE, true, versionId.getCode(), versionId.getEffectiveDate(), null, null);
    }

    public boolean hasCode() {
        return code != null;
    }

    public boolean hasMaxEffectiveDate() {
        return maxEffectiveDate != null;
    }

    public boolean hasMinEffectiveDate() {
        return minEffectiveDate != null;
    }

    public boolean hasSearchDate() {
        return searchDate != null;
    }
}
