package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.product_versions.application.params.UnpublishProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUnpublished;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.events.EventInMemoryBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnpublishProductVersionUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private UnpublishProductVersionUseCase unpublishProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    unpublishProductVersionUseCase =
        new UnpublishProductVersionUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_unpublishProductVersion_when_exists() {
    var appleV2 = ProductVersionMother.appleV2();

    var params = new UnpublishProductVersionParams(appleV2.getVersionId());

    var result = unpublishProductVersionUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isUnpublished());

    assertProductDataInDatabase(result);

    assertProductVersionWasUnpublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new UnpublishProductVersionParams(bananaV1.getVersionId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> unpublishProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productsInMemoryRepository.contains(result));
  }

  private void assertProductVersionWasUnpublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionUnpublished.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getVersionId(), event.get().getEntityId());
  }
}
