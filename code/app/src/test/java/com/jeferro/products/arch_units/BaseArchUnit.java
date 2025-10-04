package com.jeferro.products.arch_units;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseArchUnit {

  protected static final String INFRASTRUCTURE_LAYER = "..infrastructure..";

  protected static final String APPLICATION_LAYER = "..application..";

  protected static final String DOMAIN_LAYER = "..domain..";

  private static final String BASE_PACKAGE = "com.jeferro";

  protected static JavaClasses importedClasses;

  @BeforeAll
  public static void setUp() {
    importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);
  }
}
