package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.GetProductParams;
import com.jeferro.products.products.products.domain.exceptions.ProductNotFoundException;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetProductUseCaseTest {

  private GetProductUseCase getProductUseCase;

    @BeforeEach
    void beforeEach() {
	  ProductsInMemoryRepository productsInMemoryRepository = new ProductsInMemoryRepository();

        getProductUseCase = new GetProductUseCase(productsInMemoryRepository);
    }

    @Test
    void givenOneProduct_whenGetProduct_thenReturnsProduct() {
        var appleV1 = ProductMother.appleV1();

        var params = new GetProductParams(
                appleV1.getId()
        );

        var result = getProductUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(appleV1, result);
    }

    @Test
    void givenNoProducts_whenGetProduct_thenThrowsException() {
        var bananaV1 = ProductMother.bananaV1();

        var params = new GetProductParams(
                bananaV1.getId()
        );

        assertThrows(ProductNotFoundException.class,
                () -> getProductUseCase.execute(
                    ContextMother.john(),
                    params));
    }

}
