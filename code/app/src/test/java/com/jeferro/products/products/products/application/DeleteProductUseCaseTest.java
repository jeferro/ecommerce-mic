package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.DeleteProductParams;
import com.jeferro.products.products.products.domain.events.ProductDeleted;
import com.jeferro.products.products.products.domain.exceptions.ProductNotFoundException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteProductUseCaseTest {

    public ProductsInMemoryRepository productsInMemoryRepository;

    public EventInMemoryBus eventInMemoryBus;

    public DeleteProductUseCase deleteProductUseCase;

    @BeforeEach
    void beforeEach() {
        eventInMemoryBus = new EventInMemoryBus();
        productsInMemoryRepository = new ProductsInMemoryRepository();

        deleteProductUseCase = new DeleteProductUseCase(productsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenOneProduct_whenDeleteProduct_thenDeletesProduct() {
        var apple = givenAnAppleInDatabase();

        var params = new DeleteProductParams(
                apple.getId()
        );

        var result = deleteProductUseCase.execute(
            ContextMother.user(),
            params);

        assertEquals(apple, result);

        assertProductDoesNotExistInDatabase();

        assertProductDeletedWasPublished(apple);
    }

    @Test
    void givenNoProducts_whenDeleteProduct_thenThrowsException() {
        var apple = ProductMother.apple();

        var userContext = ContextMother.user();
        var params = new DeleteProductParams(
                apple.getId()
        );

        assertThrows(ProductNotFoundException.class,
                () -> deleteProductUseCase.execute(userContext, params));
    }

    private void assertProductDoesNotExistInDatabase() {
        assertTrue(productsInMemoryRepository.isEmpty());
    }

    private void assertProductDeletedWasPublished(Product product) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductDeleted) eventInMemoryBus.getFirstOrError();

        assertEquals(product.getId(), event.getId());
    }

    private Product givenAnAppleInDatabase() {
        var apple = ProductMother.apple();
        productsInMemoryRepository.init(apple);
        return apple;
    }
}
