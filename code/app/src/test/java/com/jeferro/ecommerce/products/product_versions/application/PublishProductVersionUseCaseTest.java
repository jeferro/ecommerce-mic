package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.product_versions.application.params.PublishProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionPublished;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.events.EventInMemoryBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.shared.ddd.domain.exceptions.IncorrectVersionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublishProductVersionUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private PublishProductVersionUseCase publishProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    publishProductVersionUseCase = new PublishProductVersionUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_publishProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new PublishProductVersionParams(appleV1.getId(), appleV1.getVersion());

    var result = publishProductVersionUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isPublished());

    assertProductDataInDatabase(result);

    assertProductVersionPublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new PublishProductVersionParams(bananaV1.getId(), bananaV1.getVersion());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> publishProductVersionUseCase.execute(AuthMother.john(), params));
  }

  @Test
  void should_failedAsIncorrectVersion_when_versionIsPrevious() {
    var appleV1 = ProductVersionMother.appleV1();
    var previousVersion = "v0";

    var params = new PublishProductVersionParams(appleV1.getId(), previousVersion);

    assertThrows(
        IncorrectVersionException.class,
        () -> publishProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productsInMemoryRepository.contains(result));
  }

  private void assertProductVersionPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionPublished.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }
}
