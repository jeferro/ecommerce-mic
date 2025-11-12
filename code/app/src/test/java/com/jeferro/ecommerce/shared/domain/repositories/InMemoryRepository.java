package com.jeferro.ecommerce.shared.domain.repositories;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import java.util.*;

public abstract class InMemoryRepository<
    Aggregate extends AggregateRoot<Id>, Id extends Identifier> {

  protected final Map<Id, Aggregate> data = new HashMap<>();

  public void clear() {
    data.clear();
  }

  public void save(Aggregate aggregate) {
    data.put(aggregate.getId(), aggregate);
  }

  public Optional<Aggregate> findById(Id id) {
    if (!data.containsKey(id)) {
      return Optional.empty();
    }

    var product = data.get(id);

    return Optional.of(product);
  }

  public void delete(Aggregate aggregate) {
    data.remove(aggregate.getId());
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public int size() {
    return data.size();
  }

  public boolean contains(Aggregate aggregate) {
    var saved = data.getOrDefault(aggregate.getId(), null);

    return aggregate.equals(saved);
  }

  protected List<Aggregate> paginateEntities(
      List<Aggregate> entities, DomainCriteria<?> domainCriteria) {
    int initialIndex = domainCriteria.getPageNumber() * domainCriteria.getPageSize();
    int maxIndex = entities.size() - 1;

    if (initialIndex > maxIndex) {
      return List.of();
    }

    int i = initialIndex;
    List<Aggregate> result = new ArrayList<>();

    while (i <= maxIndex) {
      result.add(entities.get(i));

      i = i + 1;
    }

    return result;
  }
}
