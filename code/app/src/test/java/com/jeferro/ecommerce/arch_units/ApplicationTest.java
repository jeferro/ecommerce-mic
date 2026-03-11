package com.jeferro.ecommerce.arch_units;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

public class ApplicationTest extends BaseArchUnit {

  private static final String PARAMS_SUFFIX = "Params";

  private static final String PARAMS_PACKAGE = "..params..";

  @Test
  public void application_can_not_depends_on_infrastructure() {
    noClasses()
        .that()
        .resideInAPackage(APPLICATION_LAYER)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(INFRASTRUCTURE_LAYER)
        .check(importedClasses);
  }

  @Test
  public void params_should_be_inside_params_package() {
    classes()
        .that()
        .resideInAPackage(APPLICATION_LAYER)
        .and()
        .haveSimpleNameEndingWith(PARAMS_SUFFIX)
        .should()
        .resideInAnyPackage(PARAMS_PACKAGE)
        .check(importedClasses);

    classes()
        .that()
        .resideInAPackage(APPLICATION_LAYER)
        .and()
        .resideInAnyPackage(PARAMS_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(PARAMS_SUFFIX)
        .check(importedClasses);
  }

  @Test
  public void services_are_not_allowed_in_application_layer() {
    noClasses()
        .that()
        .resideInAnyPackage(APPLICATION_LAYER)
        .should()
        .resideInAnyPackage("..services..")
        .check(importedClasses);
  }
}
