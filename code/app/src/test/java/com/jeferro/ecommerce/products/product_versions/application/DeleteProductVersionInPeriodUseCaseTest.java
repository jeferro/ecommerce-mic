package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.product_versions.application.params.DeleteProductVersionInPeriodParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionDeleted;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUpdated;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCodeMother;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.events.EventInMemoryBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteProductVersionInPeriodUseCaseTest {

  public ProductVersionInMemoryRepository productsInMemoryRepository;

  public EventInMemoryBus eventInMemoryBus;

  public DeleteProductVersionInPeriodUseCase deleteProductVersionInPeriodUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    deleteProductVersionInPeriodUseCase = new DeleteProductVersionInPeriodUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_deleteOverlappingVersions_when_versionsExistInRange() {
    var appleCode = ProductCodeMother.apple();
    var startDate = Instant.parse("2025-01-15T09:00:00.00Z");
    var endDate = Instant.parse("2025-02-15T09:00:00.00Z");

    var overlappingVersionId = ProductVersionId.createOf(appleCode, Instant.parse("2025-01-20T09:00:00.00Z"));
    var overlappingVersion = ProductVersion.create(
            overlappingVersionId,
            ProductVersionMother.appleV1().getTypeId(),
            ProductVersionMother.appleV1().getName(),
            ProductVersionMother.appleV1().getPrice(),
            ProductVersionMother.appleV1().getDiscount(),
            null);
    productsInMemoryRepository.save(overlappingVersion);

    var params = new DeleteProductVersionInPeriodParams(appleCode, startDate, endDate);

    deleteProductVersionInPeriodUseCase.execute(AuthMother.john(), params);

    assertProductDoesNotExistInDatabase(overlappingVersion);
    assertProductDeletedWasPublished(overlappingVersion);
  }

  @Test
  void should_updatePreviousVersion_when_previousVersionExists() {
    var appleCode = ProductCodeMother.apple();
    var startDate = Instant.parse("2025-01-15T09:00:00.00Z");
    var endDate = Instant.parse("2025-02-15T09:00:00.00Z");

    var appleV1 = ProductVersionMother.appleV1();
    productsInMemoryRepository.save(appleV1);

    var overlappingVersionId = ProductVersionId.createOf(appleCode, Instant.parse("2025-01-20T09:00:00.00Z"));
    var overlappingVersion = ProductVersion.create(
            overlappingVersionId,
            appleV1.getTypeId(),
            appleV1.getName(),
            appleV1.getPrice(),
            appleV1.getDiscount(),
            null);
    productsInMemoryRepository.save(overlappingVersion);

    var appleV2 = ProductVersionMother.appleV2();
    productsInMemoryRepository.save(appleV2);

    var params = new DeleteProductVersionInPeriodParams(appleCode, startDate, endDate);

    deleteProductVersionInPeriodUseCase.execute(AuthMother.john(), params);

    var updatedAppleV1 = productsInMemoryRepository.findByIdOrError(appleV1.getId());
    assertEquals(
        appleV2.getEffectiveDate().minusSeconds(1), updatedAppleV1.getEndEffectiveDate());
    assertProductUpdatedWasPublished(updatedAppleV1);
  }

  @Test
  void should_setEndEffectiveDateToNull_when_noNextVersionExists() {
    var appleCode = ProductCodeMother.apple();
    var startDate = Instant.parse("2025-01-15T09:00:00.00Z");
    var endDate = Instant.parse("2025-02-15T09:00:00.00Z");

    var appleV1 = ProductVersionMother.appleV1();
    productsInMemoryRepository.save(appleV1);

    var overlappingVersionId =
        ProductVersionId.createOf(appleCode, Instant.parse("2025-01-20T09:00:00.00Z"));
    var overlappingVersion =
        ProductVersion.create(
            overlappingVersionId,
            appleV1.getTypeId(),
            appleV1.getName(),
            appleV1.getPrice(),
            appleV1.getDiscount(),
            null);
    productsInMemoryRepository.save(overlappingVersion);

    var params = new DeleteProductVersionInPeriodParams(appleCode, startDate, endDate);

    deleteProductVersionInPeriodUseCase.execute(AuthMother.john(), params);

    var updatedAppleV1 = productsInMemoryRepository.findByIdOrError(appleV1.getId());
    assertNull(updatedAppleV1.getEndEffectiveDate());
    assertProductUpdatedWasPublished(updatedAppleV1);
  }

  private void assertProductDoesNotExistInDatabase(ProductVersion productVersion) {
    var product = productsInMemoryRepository.findById(productVersion.getId());

    assertTrue(product.isEmpty());
  }

  private void assertProductDeletedWasPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionDeleted.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }

  private void assertProductUpdatedWasPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }
}

