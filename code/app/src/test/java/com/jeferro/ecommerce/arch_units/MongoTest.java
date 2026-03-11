package com.jeferro.ecommerce.arch_units;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;
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

  @Test
  public void no_classes_should_instantiate_metadata_mongo_dto() {
    noClasses()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .and()
        .doNotHaveSimpleName(AuditedMongoDTO.class.getSimpleName())
        .and()
        .doNotHaveSimpleName(MetadataMongoDTO.class.getSimpleName())
        .and()
        .haveSimpleNameNotEndingWith("MapperImpl")
        .should()
        .accessClassesThat()
        .areAssignableTo(MetadataMongoDTO.class)
        .check(importedClasses);
  }
}
