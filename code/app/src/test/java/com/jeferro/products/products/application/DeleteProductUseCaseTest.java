package com.jeferro.products.products.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.products.products.application.params.DeleteProductParams;
import com.jeferro.products.products.domain.events.ProductVersionDeleted;
import com.jeferro.products.products.domain.events.ProductVersionUpdated;
import com.jeferro.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteProductUseCaseTest {

  public ProductVersionInMemoryRepository productsInMemoryRepository;

  public EventInMemoryBus eventInMemoryBus;

  public DeleteProductUseCase deleteProductUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    deleteProductUseCase = new DeleteProductUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_deleteProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new DeleteProductParams(appleV1.getVersionId());

    var result = deleteProductUseCase.execute(AuthMother.john(), params);

    assertEquals(appleV1, result);

    assertProductDoesNotExistInDatabase(appleV1);

    assertProductDeletedWasPublished(appleV1);
  }

  @Test
  void should_setEndEffectiveDateOfPreviousVersion_when_previousVersionExists() {
    var appleV2 = ProductVersionMother.appleV2();

    var params = new DeleteProductParams(appleV2.getVersionId());

    deleteProductUseCase.execute(AuthMother.john(), params);

    var appleV1Id = ProductVersionMother.appleV1().getVersionId();
    var appleV1 = productsInMemoryRepository.findByIdOrError(appleV1Id);

    assertNull(appleV1.getEndEffectiveDate());

    assertProductUpdatedWasPublished(appleV1);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new DeleteProductParams(bananaV1.getVersionId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> deleteProductUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDoesNotExistInDatabase(ProductVersion appleV1) {
    var product = productsInMemoryRepository.findById(appleV1.getVersionId());

    assertTrue(product.isEmpty());
  }

  private void assertProductDeletedWasPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionDeleted.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getVersionId(), event.get().getEntityId());
  }

  private void assertProductUpdatedWasPublished(ProductVersion previousVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(previousVersion.getVersionId(), event.get().getEntityId());
  }
}
