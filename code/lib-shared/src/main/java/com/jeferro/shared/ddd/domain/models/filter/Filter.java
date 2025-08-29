package com.jeferro.shared.ddd.domain.models.filter;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.Getter;

@Getter
public abstract class Filter<Order> extends ValueObject {

    private static final int DEFAULT_PAGE_NUMBER = 0;

    protected static final int DEFAULT_PAGE_SIZE = 100;

    public static final boolean DEFAULT_ASCENDING = true;

    private Integer pageNumber;

    private final Integer pageSize;

    private final Order order;

    private final Boolean ascending;

    public Filter(Integer pageNumber, Integer pageSize, Order order, Boolean ascending) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.order = order;
        this.ascending = ascending;
    }

    public int getPageNumber() {
        if (pageNumber == null) {
            return DEFAULT_PAGE_NUMBER;
        }

        return pageNumber;
    }

    public int getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }

        return pageSize;
    }

    public boolean isAscending() {
        if (ascending == null) {
            return DEFAULT_ASCENDING;
        }

        return ascending;
    }

    public void nextPage() {
        pageNumber = pageNumber + 1;
    }

    private boolean hasOrder() {
        return order != null;
    }
}
