package com.jeferro.shared.ddd.domain.models.filter;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class DomainCriteria<Order> extends ValueObject {

  private static final int DEFAULT_PAGE_SIZE = 100;

  public static final boolean DEFAULT_ASCENDING = true;

  private Integer pageNumber;

  private final Integer pageSize;

  private final Order order;

  private final Boolean ascending;

  public boolean isAscending() {
	return ascending != null ? ascending : DEFAULT_ASCENDING;
  }

  public void nextPage() {
	pageNumber = pageNumber + 1;
  }

  public int getPageSize() {
	return pageSize != null
		? pageSize
		: DEFAULT_PAGE_SIZE;
  }

  public int getPageNumber() {
	return pageNumber != null
		? pageNumber
		: 0;
  }
}
