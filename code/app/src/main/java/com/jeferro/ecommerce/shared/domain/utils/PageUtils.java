package com.jeferro.ecommerce.shared.domain.utils;

public abstract class PageUtils {

  public static int calculateTotalPages(long totalElements, int pageSize) {
    var numPages = totalElements % pageSize == 0
        ? totalElements / pageSize
        : (totalElements / pageSize) + 1;

    return (int) numPages;
  }
}
