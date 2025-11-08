package com.jeferro.shared.mongo;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public abstract class MongoDao<DTO extends AuditedMongoDTO, ID, C extends DomainCriteria<?>> {

  private final MongoTemplate mongoTemplate;

  protected MongoDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public abstract Class<DTO> getEntityClass();

  public void save(DTO dto) {
    var username = ContextManager.getAuth().getUsername();
    dto.markAsSavedBy(username);

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

  public List<DTO> findAll(C criteria) {
    var entityClass = getEntityClass();

    return findAll(criteria, entityClass, List.of());
  }

  public <Summary> List<Summary> findAll(
      C criteria, Class<Summary> entityClass, List<String> projections) {
    var query = createDataQuery(criteria);

    if (ObjectUtils.isNotEmpty(projections)) {
      var fields = query.fields();
      projections.forEach(fields::include);
    }

    return mongoTemplate.find(query, entityClass);
  }

  public long count(C criteria) {
    var entityClass = getEntityClass();

    var countQuery = createCountQuery(criteria);

    return mongoTemplate.count(countQuery, entityClass);
  }

  private Query createCountQuery(C criteria) {
    var countQuery = new Query();

    mapCriteria(criteria).forEach(countQuery::addCriteria);

    return countQuery;
  }

  private Query createDataQuery(C criteria) {
    var dataQuery = new Query();

    mapCriteria(criteria).forEach(dataQuery::addCriteria);

    var sortBy = mapOrder(criteria);
    var sortDirection = criteria.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
    var sort = Sort.by(sortDirection, sortBy);
    dataQuery.with(sort);

    int pageNumber = criteria.getPageNumber();
    int pageSize = criteria.getPageSize();
    var pageable = PageRequest.of(pageNumber, pageSize);
    dataQuery.with(pageable);

    return dataQuery;
  }

  protected abstract List<Criteria> mapCriteria(C domainCriteria);

  protected abstract String mapOrder(C domainCriteria);
}
