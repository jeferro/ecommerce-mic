package com.jeferro.products.products.products.domain.models.filter;

import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.models.filter.Filter;
import lombok.Getter;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.filter.ProductFilterOrder.NAME;
import static com.jeferro.products.products.products.domain.models.filter.ProductFilterOrder.START_EFFECTIVE_DATE;

@Getter
public class ProductFilter extends Filter<ProductFilterOrder> {

    private final String name;

    private final ProductCode code;

    private final Instant minEffectiveDate;

    public ProductFilter(Integer pageNumber, Integer pageSize, ProductFilterOrder order, Boolean ascending, String name, ProductCode code,
        Instant minEffectiveDate) {
        super(pageNumber, pageSize, order, ascending);

        this.name = name;
        this.code = code;
        this.minEffectiveDate = minEffectiveDate;
    }

    public static ProductFilter createEmpty() {
        return new ProductFilter(null, null, NAME, null, null, null, null);
    }

    public static ProductFilter searchName(String name) {
        return new ProductFilter(null, null, NAME, false, name, null, null);
    }

    public static ProductFilter nextProduct(ProductId id) {
        return new ProductFilter(0, 1, START_EFFECTIVE_DATE, false, null, id.getCode(), id.getEffectiveDate());
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasCode() {
        return code != null;
    }

    public boolean hasMinEffectiveDate() {
        return minEffectiveDate != null;
    }
}
