package com.jeferro.shared.auth.infrastructure.mongo.configurations;

import com.jeferro.shared.auth.infrastructure.mongo.services.CustomMongoTemplate;
import com.jeferro.shared.auth.infrastructure.mongo.services.MongoAuditorAware;
import com.jeferro.shared.auth.infrastructure.mongo.services.MongoFieldManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration {

  @Bean
  @Primary
  public MongoTemplate mongoTemplate(
      MongoDatabaseFactory mongoDatabaseFactory,
      MongoAuditorAware mongoAuditorAware,
      MongoFieldManager mongoFieldManager) {
    return new CustomMongoTemplate(mongoDatabaseFactory, mongoAuditorAware, mongoFieldManager);
  }
}
