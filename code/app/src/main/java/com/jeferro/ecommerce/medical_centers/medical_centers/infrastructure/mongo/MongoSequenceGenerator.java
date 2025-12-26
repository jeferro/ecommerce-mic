package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.SequenceGenerator;
import org.springframework.stereotype.Component;

@Component
public class MongoSequenceGenerator implements SequenceGenerator {

  private final com.jeferro.shared.mongo.sequence.SequenceGenerator sharedSequenceGenerator;

  public MongoSequenceGenerator(com.jeferro.shared.mongo.sequence.SequenceGenerator sharedSequenceGenerator) {
    this.sharedSequenceGenerator = sharedSequenceGenerator;
  }

  @Override
  public <T> String generate(Class<T> entityClass) {
    return sharedSequenceGenerator.generate(entityClass);
  }
}

