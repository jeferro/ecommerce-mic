package com.jeferro.products.arch_units;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.junit.jupiter.api.Test;

public class InfrastructureTest extends BaseArchUnit {

  private static final String DTO_SUFFIX = "DTO";

  private static final String MAPPER_SUFFIX = "Mapper";

  private static final String MAPPER_IMPL_SUFFIX = "MapperImpl";

  private static final String DTO_PACKAGE = "..dtos..";

  private static final String MAPPER_PACKAGE = "..mappers..";

  @Test
  public void dtos_should_be_inside_dtos_package() {
    classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .and()
        .haveSimpleNameEndingWith(DTO_SUFFIX)
        .and()
        .haveSimpleNameNotContaining("Builder")
        .should()
        .resideInAnyPackage(DTO_PACKAGE)
        .check(importedClasses);

    classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .and()
        .resideInAnyPackage(DTO_PACKAGE)
        .and()
        .haveSimpleNameNotContaining("Builder")
        .should()
        .haveSimpleNameEndingWith(DTO_SUFFIX)
        .check(importedClasses);
  }

  @Test
  public void mappers_should_be_inside_mappers_package() {
    classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .and()
        .areNotAnonymousClasses()
        .and()
        .haveSimpleNameNotContaining("$")
        .and()
        .haveSimpleNameEndingWith(MAPPER_SUFFIX)
        .or()
        .haveSimpleNameEndingWith(MAPPER_IMPL_SUFFIX)
        .should()
        .resideInAnyPackage(MAPPER_PACKAGE)
        .check(importedClasses);

    classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_LAYER)
        .and()
        .areNotAnonymousClasses()
        .and()
        .haveSimpleNameNotContaining("$")
        .and()
        .resideInAnyPackage(MAPPER_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(MAPPER_SUFFIX)
        .orShould()
        .haveSimpleNameEndingWith(MAPPER_IMPL_SUFFIX)
        .check(importedClasses);
  }
}
