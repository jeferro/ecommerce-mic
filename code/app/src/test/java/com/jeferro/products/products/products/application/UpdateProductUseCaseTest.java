package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.UpdateProductParams;
import com.jeferro.products.products.products.domain.events.ProductUpdated;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductUseCaseTest {

    private ProductsInMemoryRepository productsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private UpdateProductUseCase updateProductUseCase;

    @BeforeEach
    void beforeEach() {
        eventInMemoryBus = new EventInMemoryBus();
        productsInMemoryRepository = new ProductsInMemoryRepository();

        updateProductUseCase = new UpdateProductUseCase(productsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenOneProduct_whenUpdateProduct_thenUpdatesProduct() {
        var appleV1 = ProductMother.appleV1();

        var newName = LocalizedField.createOf("en-US", "new product name");
        var params = new UpdateProductParams(
                appleV1.getId(),
                newName
        );

        var result = updateProductUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(newName, result.getName());

        assertProductDataInDatabase(result);

        assertProductUpdatedWasPublished(result);
    }

    @Test
    void givenNoProducts_whenUpdateProduct_thenThrowsException() {
        var bananaV1 = ProductMother.bananaV1();

        var newName = LocalizedField.createOf("en-US", "new product name");
        var params = new UpdateProductParams(
                bananaV1.getId(),
                newName
        );

        assertThrows(ProductVersionNotFoundException.class,
                () -> updateProductUseCase.execute(
                    ContextMother.john(),
                    params));
    }

    private void assertProductDataInDatabase(Product result) {
        assertTrue(productsInMemoryRepository.contains(result));
    }

    private void assertProductUpdatedWasPublished(Product product) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductUpdated) eventInMemoryBus.getFirstOrError();

        assertEquals(product.getId(), event.getId());
    }
}
