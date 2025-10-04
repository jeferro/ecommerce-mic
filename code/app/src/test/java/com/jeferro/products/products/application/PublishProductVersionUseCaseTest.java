package com.jeferro.products.products.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.products.products.application.params.PublishProductParams;
import com.jeferro.products.products.domain.events.ProductVersionPublished;
import com.jeferro.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublishProductVersionUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private PublishProductUseCase publishProductUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    publishProductUseCase = new PublishProductUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_publishProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new PublishProductParams(appleV1.getVersionId());

    var result = publishProductUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isPublished());

    assertProductDataInDatabase(result);

    assertProductVersionPublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new PublishProductParams(bananaV1.getVersionId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> publishProductUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productsInMemoryRepository.contains(result));
  }

  private void assertProductVersionPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionPublished.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getVersionId(), event.get().getVersionId());
  }
}
