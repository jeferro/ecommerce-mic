package com.jeferro.products.shared.domain.utils;

public abstract class PageUtils {

  public static long calculateTotalPages(long totalElements, int pageSize) {
    if (totalElements % pageSize == 0) {
      return totalElements / pageSize;
    }

    return totalElements / pageSize + 1;
  }
}
