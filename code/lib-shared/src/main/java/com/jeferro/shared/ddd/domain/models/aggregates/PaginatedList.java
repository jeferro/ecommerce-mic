package com.jeferro.shared.ddd.domain.models.aggregates;

import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class PaginatedList<T> extends ValueObject implements Collection<T> {

  private final List<T> items;

  private final Integer pageNumber;

  private final Integer pageSize;

  private final Long totalItems;

  public PaginatedList(List<T> items, Integer pageNumber, Integer pageSize, Long totalItems) {
    this.items = items;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalItems = totalItems;
  }

  public static <T> PaginatedList<T> createOfItems(T... items) {
    var itemsList = Arrays.asList(items);

    return PaginatedList.createOfList(itemsList);
  }

  public static <T> PaginatedList<T> createOfList(List<T> items) {
    items = items != null ? new ArrayList<>(items) : new ArrayList<>();

    int pageSize = items.size();
    long totalItems = items.size();

    return new PaginatedList<T>(items, 0, pageSize, totalItems);
  }

  public static <T> PaginatedList<T> createOfCriteria(
      DomainCriteria<?> criteria, List<T> items, long totalElements) {
    return new PaginatedList<>(
        items, criteria.getPageNumber(), criteria.getPageSize(), totalElements);
  }

  @Override
  public boolean isEmpty() {
    return items.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return items.contains(o);
  }

  public boolean isNotEmpty() {
    return !items.isEmpty();
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  public Iterator<T> iterator() {
    return items.iterator();
  }

  @Override
  public Object[] toArray() {
    return items.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return items.toArray(a);
  }

  @Override
  public boolean remove(Object o) {
    return items.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return items.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return items.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return items.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return items.retainAll(c);
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public boolean add(T entity) {
    items.add(entity);
    return true;
  }

  public T get(int index) {
    return items.get(index);
  }

  public Stream<T> filter(Predicate<? super T> predicate) {
    return items.stream().filter(predicate);
  }

  public <R> Stream<R> map(Function<T, ? extends R> mapper) {
    return items.stream().map(mapper);
  }

  public boolean isPageable() {
    return pageNumber != null && pageSize != null;
  }

  public boolean isNotPageable() {
    return !isPageable();
  }

  public T getFirstOrNull() {
    return items.isEmpty() ? null : items.getFirst();
  }

  public long getTotalPages() {
    if (isNotPageable()) {
      return 1;
    }

    if (totalItems % pageSize == 0) {
      return totalItems / pageSize;
    }

    return totalItems / pageSize + 1;
  }
}
