package com.jeferro.products.products.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.products.products.application.params.UnpublishProductParams;
import com.jeferro.products.products.domain.events.ProductVersionUnpublished;
import com.jeferro.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnpublishProductVersionUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private UnpublishProductUseCase unpublishProductUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    unpublishProductUseCase =
        new UnpublishProductUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_unpublishProductVersion_when_exists() {
    var appleV2 = ProductVersionMother.appleV2();

    var params = new UnpublishProductParams(appleV2.getVersionId());

    var result = unpublishProductUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isUnpublished());

    assertProductDataInDatabase(result);

    assertProductVersionWasUnpublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new UnpublishProductParams(bananaV1.getVersionId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> unpublishProductUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productsInMemoryRepository.contains(result));
  }

  private void assertProductVersionWasUnpublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionUnpublished.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getVersionId(), event.get().getVersionId());
  }
}
