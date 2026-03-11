package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.product_versions.application.params.DeleteProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionDeleted;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUpdated;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionFakeRepository;
import com.jeferro.ecommerce.shared.domain.events.EventFakeBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteProductVersionUseCaseTest {

  public ProductVersionFakeRepository productVersionFakeRepository;

  public EventFakeBus eventFakeBus;

  public DeleteProductVersionUseCase deleteProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventFakeBus = new EventFakeBus();
    productVersionFakeRepository = new ProductVersionFakeRepository();

    deleteProductVersionUseCase = new DeleteProductVersionUseCase(productVersionFakeRepository, eventFakeBus);
  }

  @Test
  void should_deleteProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new DeleteProductVersionParams(appleV1.getId());

    var result = deleteProductVersionUseCase.execute(AuthMother.john(), params);

    assertEquals(appleV1, result);

    assertProductDoesNotExistInDatabase(appleV1);

    assertProductDeletedWasPublished(appleV1);
  }

  @Test
  void should_setEndEffectiveDateOfPreviousVersion_when_previousVersionExists() {
    var appleV2 = ProductVersionMother.appleV2();

    var params = new DeleteProductVersionParams(appleV2.getId());

    deleteProductVersionUseCase.execute(AuthMother.john(), params);

    var appleV1Id = ProductVersionMother.appleV1().getId();
    var appleV1 = productVersionFakeRepository.findByIdOrError(appleV1Id);

    assertNull(appleV1.getEndEffectiveDate());

    assertProductUpdatedWasPublished(appleV1);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new DeleteProductVersionParams(bananaV1.getId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> deleteProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDoesNotExistInDatabase(ProductVersion appleV1) {
    var product = productVersionFakeRepository.findById(appleV1.getId());

    assertTrue(product.isEmpty());
  }

  private void assertProductDeletedWasPublished(ProductVersion productVersion) {
    var event = eventFakeBus.filterOfClass(ProductVersionDeleted.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }

  private void assertProductUpdatedWasPublished(ProductVersion previousVersion) {
    var event = eventFakeBus.filterOfClass(ProductVersionUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(previousVersion.getId(), event.get().getEntityId());
  }
}
