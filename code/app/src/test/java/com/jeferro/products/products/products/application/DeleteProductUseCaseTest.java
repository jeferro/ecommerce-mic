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
        var appleV1 = ProductMother.appleV1();

        var params = new DeleteProductParams(
                appleV1.getId()
        );

        var result = deleteProductUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(appleV1, result);

        assertProductDoesNotExistInDatabase(appleV1);

        assertProductDeletedWasPublished(appleV1);
    }

    @Test
    void givenUnknownProduct_whenDeleteProduct_thenThrowsException() {
        var bananaV1 = ProductMother.bananaV1();

        var params = new DeleteProductParams(
                bananaV1.getId()
        );

        assertThrows(ProductNotFoundException.class,
                () -> deleteProductUseCase.execute(
                    ContextMother.john(),
                    params));
    }

    private void assertProductDoesNotExistInDatabase(Product appleV1) {
        var product = productsInMemoryRepository.findById(appleV1.getId());

        assertTrue(product.isEmpty());
    }

    private void assertProductDeletedWasPublished(Product product) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductDeleted) eventInMemoryBus.getFirstOrError();

        assertEquals(product.getId(), event.getId());
    }
}
