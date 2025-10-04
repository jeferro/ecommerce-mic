package com.jeferro.shared.mongo.sequence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class SequenceGenerator {

  private final MongoTemplate mongoTemplate;

  public SequenceGenerator(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public <T> String generate(Class<T> entityClass) {
    String collectionName = mongoTemplate.getCollectionName(entityClass);

    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(collectionName));

    Update update = new Update();
    update.inc("value");

    FindAndModifyOptions options = new FindAndModifyOptions();
    options.returnNew(true);
    options.upsert(true);

    var sequence = mongoTemplate.findAndModify(query, update, options, SequenceMongoDocument.class);

    var value = sequence != null ? sequence.value() : "1";

    return StringUtils.leftPad(value, 7, '0');
  }
}
