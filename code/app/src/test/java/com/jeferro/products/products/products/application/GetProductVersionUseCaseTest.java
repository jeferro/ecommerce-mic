package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.GetProductParams;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetProductVersionUseCaseTest {

  private GetProductUseCase getProductUseCase;

    @BeforeEach
    void beforeEach() {
	  ProductVersionInMemoryRepository productsInMemoryRepository = new ProductVersionInMemoryRepository();

        getProductUseCase = new GetProductUseCase(productsInMemoryRepository);
    }

    @Test
    void should_returnProductVersion_when_exists() {
        var appleV1 = ProductVersionMother.appleV1();

        var params = new GetProductParams(
                appleV1.getVersionId()
        );

        var result = getProductUseCase.execute(
            AuthMother.john(),
            params);

        assertEquals(appleV1, result);
    }

    @Test
    void should_failedAsUnknownProductVersion_when_notExist() {
        var bananaV1 = ProductVersionMother.bananaV1();

        var params = new GetProductParams(
                bananaV1.getVersionId()
        );

        assertThrows(ProductVersionNotFoundException.class,
                () -> getProductUseCase.execute(
                    AuthMother.john(),
                    params));
    }

}
