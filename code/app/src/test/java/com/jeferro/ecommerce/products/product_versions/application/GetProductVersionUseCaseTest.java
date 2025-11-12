package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jeferro.ecommerce.products.product_versions.application.params.GetProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetProductVersionUseCaseTest {

  private GetProductVersionUseCase getProductVersionUseCase;

  @BeforeEach
  void beforeEach() {
    ProductVersionInMemoryRepository productsInMemoryRepository =
        new ProductVersionInMemoryRepository();

    getProductVersionUseCase = new GetProductVersionUseCase(productsInMemoryRepository);
  }

  @Test
  void should_returnProductVersion_when_exists() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new GetProductVersionParams(appleV1.getVersionId());

    var result = getProductVersionUseCase.execute(AuthMother.john(), params);

    assertEquals(appleV1, result);
  }

  @Test
  void should_failedAsUnknownProductVersion_when_notExist() {
    var bananaV1 = ProductVersionMother.bananaV1();

    var params = new GetProductVersionParams(bananaV1.getVersionId());

    assertThrows(
        ProductVersionNotFoundException.class,
        () -> getProductVersionUseCase.execute(AuthMother.john(), params));
  }
}
