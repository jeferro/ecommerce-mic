package com.jeferro.ecommerce.products.product_versions.application;

import com.jeferro.ecommerce.products.product_versions.application.params.UnpublishProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUnpublished;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionFakeRepository;
import com.jeferro.ecommerce.shared.domain.events.EventFakeBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.shared.ddd.domain.exceptions.IncorrectVersionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class UnpublishProductVersionUseCaseTest {

  private ProductVersionFakeRepository productVersionFakeRepository;

  private EventFakeBus eventFakeBus;

  private UnpublishProductVersionUseCase unpublishProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventFakeBus = new EventFakeBus();
    productVersionFakeRepository = new ProductVersionFakeRepository();

    unpublishProductVersionUseCase =
        new UnpublishProductVersionUseCase(productVersionFakeRepository, eventFakeBus);
  }

  @Test
  void should_unpublishProductVersion_when_exists() {
    var appleV2 = ProductVersionMother.appleV2();

    var params = new UnpublishProductVersionParams(appleV2.getId(), appleV2.getVersion());

    var result = unpublishProductVersionUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isUnpublished());

    assertProductDataInDatabase(result);

    assertProductVersionWasUnpublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new UnpublishProductVersionParams(bananaV1.getId(), bananaV1.getVersion());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> unpublishProductVersionUseCase.execute(AuthMother.john(), params));
  }

  @Test
  void should_failedAsIncorrectVersion_when_versionIsPrevious() {
    var appleV2 = ProductVersionMother.appleV2();
    var previousVersion = 0;

    var params = new UnpublishProductVersionParams(appleV2.getId(), previousVersion);

    assertThrows(
        IncorrectVersionException.class,
        () -> unpublishProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productVersionFakeRepository.contains(result));
  }

  private void assertProductVersionWasUnpublished(ProductVersion productVersion) {
    var event = eventFakeBus.filterOfClass(ProductVersionUnpublished.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }
}
