package com.jeferro.shared.mongo;

import java.util.List;
import java.util.Optional;

import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public abstract class MongoDao<DTO, ID, C extends DomainCriteria<?>> {

  private final MongoTemplate mongoTemplate;

  protected MongoDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public abstract Class<DTO> getEntityClass();

  public void save(DTO dto) {
    mongoTemplate.save(dto);
  }

  public Optional<DTO> findById(ID id) {
    var entityClass = getEntityClass();

    var dto = mongoTemplate.findById(id, entityClass);

    return Optional.ofNullable(dto);
  }

  public void delete(DTO dto) {
    mongoTemplate.remove(dto);
  }

  public void deleteAllById(Iterable<ID> ids) {
    var idCriteria = Criteria.where("_id").in(ids);

    var query = new Query();
    query.addCriteria(idCriteria);

    mongoTemplate.remove(query);
  }

  public Page<DTO> findAllByCriteria(C criteria) {
    var entityClass = getEntityClass();

    var query = mapCriteriaToQuery(criteria);

    long count = mongoTemplate.count(query, entityClass);

    var pageable = mapCriteriaToPageable(criteria);
    query.with(pageable);

    var sort = mapCriteriaToSort(criteria);
    query.with(sort);

    List<DTO> entities = mongoTemplate.find(query, entityClass);

    return new PageImpl<>(entities, pageable, count);
  }

  public <Summary> Page<Summary> findAllByCriteria(C criteria, Class<Summary> entityClass, List<String> projections) {
    var query = mapCriteriaToQuery(criteria);

    long count = mongoTemplate.count(query, entityClass);

    var pageable = mapCriteriaToPageable(criteria);
    query.with(pageable);

    var sort = mapCriteriaToSort(criteria);
    query.with(sort);

    var fields = query.fields();

    projections.forEach(fields::include);

    List<Summary> entities = mongoTemplate.find(query, entityClass);

    return new PageImpl<>(entities, pageable, count);
  }

  private Query mapCriteriaToQuery(C domainCriteria) {
    Query query = new Query();

    mapCriteria(domainCriteria).forEach(query::addCriteria);

    return query;
  }

  private Sort mapCriteriaToSort(C domainCriteria) {
    String sortBy = mapOrder(domainCriteria);
    Sort.Direction sortDirection = domainCriteria.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
    return Sort.by(sortDirection, sortBy);
  }

  private Pageable mapCriteriaToPageable(C domainCriteria) {
    int pageNumber = domainCriteria.getPageNumber();
    int pageSize = domainCriteria.getPageSize();

    return PageRequest.of(pageNumber, pageSize);
  }

  protected abstract List<Criteria> mapCriteria(C domainCriteria);

  protected abstract String mapOrder(C domainCriteria);
}
