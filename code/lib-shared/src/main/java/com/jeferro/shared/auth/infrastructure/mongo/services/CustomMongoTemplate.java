package com.jeferro.shared.auth.infrastructure.mongo.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class CustomMongoTemplate extends MongoTemplate {

  private static final String ID = "id";

  private final MongoAuditorAware mongoAuditorAware;

  private final MongoFieldManager mongoFieldManager;

  public CustomMongoTemplate(
      MongoDatabaseFactory mongoDatabaseFactory,
      MongoAuditorAware mongoAuditorAware,
      MongoFieldManager mongoFieldManager) {
    super(mongoDatabaseFactory);

    this.mongoAuditorAware = mongoAuditorAware;
    this.mongoFieldManager = mongoFieldManager;
  }

  @Override
  public <T> T save(T objectToSave, String collectionName) {
    final Update update = new Update();
    updateAllFields(update, objectToSave);
    updateMetadataFields(update);

    Criteria criteria = Criteria.where(ID).is(mongoFieldManager.getFieldIdValue(objectToSave));
    final Query query = new Query(criteria);

    upsert(query, update, objectToSave.getClass());

    return objectToSave;
  }

  public <T> Page<T> findPage(Query query, Class<T> entityClass) {
    List<T> content = find(query, entityClass);

    if (!query.isLimited()) {
      return new PageImpl<>(content);
    }

    PageRequest pageRequest = createPageRequestFromQuery(query);

    Query unlimitedQuery = Query.of(query).limit(0).skip(0);

    long count = count(unlimitedQuery, entityClass);

    return new PageImpl<>(content, pageRequest, count);
  }

  private void updateAllFields(Update update, Object objectToSave) {
    final Map<String, Object> fieldsToUpdate =
        mongoFieldManager.calculateFieldToUpdate(objectToSave);

    if (fieldsToUpdate.isEmpty()) {
      throw new RuntimeException(
          "No updatable fields found in the document of type "
              + objectToSave.getClass().getSimpleName());
    }

    fieldsToUpdate.forEach(update::set);
  }

  private void updateMetadataFields(Update update) {
    Instant now = Instant.now();
    String username = mongoAuditorAware.getCurrentAuditor().orElseThrow();

    update.setOnInsert("metadata.createdBy", username);
    update.setOnInsert("metadata.createdAt", now);

    update.set("metadata.updatedAt", now);
    update.set("metadata.updatedBy", username);
  }

  private static PageRequest createPageRequestFromQuery(Query query) {
    int pageSize = query.getLimit();
    int pageNumber = ((int) query.getSkip()) / pageSize;

    return PageRequest.of(pageNumber, pageSize);
  }
}
