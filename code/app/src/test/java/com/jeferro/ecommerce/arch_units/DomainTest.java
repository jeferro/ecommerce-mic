package com.jeferro.ecommerce.arch_units;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import org.junit.jupiter.api.Test;

public class DomainTest extends BaseArchUnit {

  private static final String EXCEPTION_SUFFIX = "Exception";

  private static final String EXCEPTION_CODES_SUFFIX = "ExceptionCodes";

  private static final String EXCEPTIONS_PACKAGE = "..exceptions..";

  @Test
  public void domain_can_not_depends_on_application_or_infrastructure() {
    noClasses()
        .that()
        .resideInAPackage(DOMAIN_LAYER)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(INFRASTRUCTURE_LAYER, APPLICATION_LAYER)
        .check(importedClasses);
  }

  @Test
  public void exception_should_be_inside_exceptions_package() {
    classes()
        .that()
        .resideInAPackage(DOMAIN_LAYER)
        .and()
        .haveSimpleNameEndingWith(EXCEPTION_SUFFIX)
        .should()
        .resideInAnyPackage(EXCEPTIONS_PACKAGE)
        .check(importedClasses);

    classes()
        .that()
        .resideInAPackage(DOMAIN_LAYER)
        .and()
        .resideInAnyPackage(EXCEPTIONS_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(EXCEPTION_SUFFIX)
        .orShould()
        .haveSimpleNameEndingWith(EXCEPTION_CODES_SUFFIX)
        .check(importedClasses);
  }

  @Test
  public void no_classes_should_instantiate_metadata() {
    noClasses()
        .that()
        .resideInAPackage(DOMAIN_LAYER)
        .and()
        .doNotHaveSimpleName(Metadata.class.getSimpleName())
        .should()
        .accessClassesThat()
        .areAssignableTo(Metadata.class)
        .check(importedClasses);
  }
}
