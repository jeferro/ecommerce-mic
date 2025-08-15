package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.GetProductParams;
import com.jeferro.products.products.products.domain.exceptions.ProductNotFoundException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetProductUseCaseTest {

    private ProductsInMemoryRepository productsInMemoryRepository;

    private GetProductUseCase getProductUseCase;

    @BeforeEach
    void beforeEach() {
        productsInMemoryRepository = new ProductsInMemoryRepository();

        getProductUseCase = new GetProductUseCase(productsInMemoryRepository);
    }

    @Test
    void givenOneProduct_whenGetProduct_thenReturnsProduct() {
        var apple = givenAnAppleInDatabase();

        var params = new GetProductParams(
                apple.getId()
        );

        var result = getProductUseCase.execute(
            ContextMother.user(),
            params);

        assertEquals(apple, result);
    }

    @Test
    void givenNoProducts_whenGetProduct_thenThrowsException() {
        var apple = ProductMother.apple();

        var userContext = ContextMother.user();
        var params = new GetProductParams(
                apple.getId()
        );

        assertThrows(ProductNotFoundException.class,
                () -> getProductUseCase.execute(userContext, params));
    }

    private Product givenAnAppleInDatabase() {
        var apple = ProductMother.apple();
        productsInMemoryRepository.init(apple);
        return apple;
    }

}
