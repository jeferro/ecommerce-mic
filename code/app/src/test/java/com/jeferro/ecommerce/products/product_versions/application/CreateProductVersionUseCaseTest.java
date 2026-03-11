package com.jeferro.ecommerce.products.product_versions.application;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCodeMother;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionFakeRepository;
import com.jeferro.ecommerce.products.product_versions.application.params.CreateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionCreated;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUpdated;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.shared.domain.events.EventFakeBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.ecommerce.support.parametrics.domain.models.ProductTypeMother;
import com.jeferro.ecommerce.support.parametrics.domain.services.ParametricFakeFinder;
import com.jeferro.ecommerce.support.parametrics.domain.services.ParametricValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.jeferro.ecommerce.products.product_versions.domain.models.status.ProductStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CreateProductVersionUseCaseTest {

  private ProductVersionFakeRepository productVersionFakeRepository;

  private EventFakeBus eventFakeBus;

  private CreateProductVersionUseCase createProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventFakeBus = new EventFakeBus();
    productVersionFakeRepository = new ProductVersionFakeRepository();

    var parametricFinder = new ParametricFakeFinder();
    var parametricValidator = new ParametricValidator(parametricFinder);

    createProductVersionUseCase =
        new CreateProductVersionUseCase(productVersionFakeRepository, parametricValidator, eventFakeBus);
  }

  @Test
  void should_createVersion_when_versionNotExists() {
    var pearV2 = pearV2();

    var params =
        new CreateProductVersionParams(pearV2.getId(), pearV2.getTypeId(), pearV2.getName(),
                pearV2.getPrice(),
                pearV2.getDiscount());

    var result = createProductVersionUseCase.execute(AuthMother.john(), params);

    assertNull(result.getEndEffectiveDate());

    assertTrue(productVersionFakeRepository.contains(result));

    assertProductCreatedWasPublished(result);
  }

  @Test
  void should_setEndEffectiveDateOfPreviousVersion_when_previousVersionExists() {
    var pearV2 = pearV2();

    var params =
        new CreateProductVersionParams(pearV2.getId(), pearV2.getTypeId(), pearV2.getName(),
                pearV2.getPrice(),
                pearV2.getDiscount());

    createProductVersionUseCase.execute(AuthMother.john(), params);

    var pearV1Id = ProductVersionMother.pearV1().getId();
    var pearV1 = productVersionFakeRepository.findByIdOrError(pearV1Id);

    assertEquals(
        pearV2.getEffectiveDate().minus(1, ChronoUnit.SECONDS), pearV1.getEndEffectiveDate());

    assertProductUpdatedWasPublished(pearV1);
  }

  @Test
  void should_setEndEffectiveDate_when_nextVersionExists() {
    var previousPearV2 = previousPearV2();

    var params =
        new CreateProductVersionParams(
            previousPearV2.getId(), previousPearV2.getTypeId(), previousPearV2.getName(),
                previousPearV2.getPrice(),
                previousPearV2.getDiscount());

    var result = createProductVersionUseCase.execute(AuthMother.john(), params);

    assertTrue(productVersionFakeRepository.contains(result));

    assertEndEffectiveDateOfPreviousVersion(result);
  }

  @Test
  void should_failedAsDuplicatedVersion_when_versionExists() {
    var pearV1 = ProductVersionMother.pearV1();

    var params =
        new CreateProductVersionParams(pearV1.getId(), pearV1.getTypeId(), pearV1.getName(),
                pearV1.getPrice(),
                pearV1.getDiscount());

    assertThrows(
        ProductVersionAlreadyExistsException.class,
        () -> createProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductCreatedWasPublished(ProductVersion result) {
    var event = eventFakeBus.filterOfClass(ProductVersionCreated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(result.getId(), event.get().getEntityId());
  }

  private void assertProductUpdatedWasPublished(ProductVersion previous) {
    var event = eventFakeBus.filterOfClass(ProductVersionUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(previous.getId(), event.get().getEntityId());
  }

  private void assertEndEffectiveDateOfPreviousVersion(ProductVersion result) {
    var pearV1Id = ProductVersionMother.pearV1().getId();
    var pearV1 = productVersionFakeRepository.findByIdOrError(pearV1Id);

    assertEquals(
        pearV1.getEffectiveDate().minus(1, ChronoUnit.SECONDS), result.getEndEffectiveDate());
  }

  public ProductVersion pearV2() {
    var productCode = ProductCodeMother.pear();
    var effectiveDate = Instant.parse("2025-02-01T09:00:00.00Z");
    var productId = ProductVersionId.createOf(productCode, effectiveDate);

    var fruitId = ProductTypeMother.fruitId();
    var name =
        LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

    return new ProductVersion(productId, name, fruitId, null,
            new BigDecimal("2"),
            new BigDecimal("0"),
            new BigDecimal("2"),
            PUBLISHED,
            2L, null);
  }

  public ProductVersion previousPearV2() {
    var productCode = ProductCodeMother.pear();
    var effectiveDate = ProductVersionMother.pearV1().getEffectiveDate().minus(60, ChronoUnit.DAYS);
    var productId = ProductVersionId.createOf(productCode, effectiveDate);

    var fruitId = ProductTypeMother.fruitId();
    var name =
        LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

    return new ProductVersion(productId, name, fruitId, null,
            new BigDecimal("2"),
            new BigDecimal("0"),
            new BigDecimal("2"),
            PUBLISHED,
            2L, null);
  }
}
