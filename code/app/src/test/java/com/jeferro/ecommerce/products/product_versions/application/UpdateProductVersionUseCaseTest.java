package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.product_versions.application.params.UpdateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUpdated;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.events.EventInMemoryBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.shared.ddd.domain.exceptions.IncorrectVersionException;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateProductVersionUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private UpdateProductVersionUseCase updateProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    eventInMemoryBus = new EventInMemoryBus();
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    updateProductVersionUseCase = new UpdateProductVersionUseCase(productsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_updateProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var newName = LocalizedField.createOf("en-US", "new product name");
    var params = new UpdateProductVersionParams(appleV1.getId(), newName,
            appleV1.getPrice(),
            appleV1.getDiscount(),
            appleV1.getVersion());

    var result = updateProductVersionUseCase.execute(AuthMother.john(), params);

    assertEquals(newName, result.getName());

    assertProductDataInDatabase(result);

    assertProductUpdatedWasPublished(result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var newName = LocalizedField.createOf("en-US", "new product name");
    var params = new UpdateProductVersionParams(bananaV1.getId(), newName,
            bananaV1.getPrice(),
            bananaV1.getDiscount(),
            bananaV1.getVersion());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> updateProductVersionUseCase.execute(AuthMother.john(), params));
  }

  @Test
  void should_failedAsIncorrectVersion_when_versionIsPrevious() {
    var appleV1 = ProductVersionMother.appleV1();
    var previousVersion = 0;


    var newName = LocalizedField.createOf("en-US", "new product name");
    var params = new UpdateProductVersionParams(appleV1.getId(), newName,
            appleV1.getPrice(),
            appleV1.getDiscount(),
            previousVersion);

    assertThrows(
        IncorrectVersionException.class,
        () -> updateProductVersionUseCase.execute(AuthMother.john(), params));
  }

  private void assertProductDataInDatabase(ProductVersion result) {
    assertTrue(productsInMemoryRepository.contains(result));
  }

  private void assertProductUpdatedWasPublished(ProductVersion productVersion) {
    var event = eventInMemoryBus.filterOfClass(ProductVersionUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(productVersion.getId(), event.get().getEntityId());
  }
}
