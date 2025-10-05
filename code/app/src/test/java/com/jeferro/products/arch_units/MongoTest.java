package com.jeferro.products.arch_units;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public class MongoTest extends BaseArchUnit {

  @Test
  public void no_classes_should_extend_mongo_repository() {
    classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .should()
        .notBeAssignableTo(MongoRepository.class)
        .check(importedClasses);
  }

  @Test
  public void document_classes_should_extend_audited_mongo_dto() {
    classes()
        .that()
        .areAnnotatedWith(Document.class)
        .should()
        .beAssignableTo(AuditedMongoDTO.class)
        .check(importedClasses);
  }
}
