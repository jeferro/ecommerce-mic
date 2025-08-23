package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.UpdateProductParams;
import com.jeferro.products.products.products.domain.events.ProductVersionUpdated;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionNotFoundException;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class UpdateProductVersionUseCaseTest {

    private ProductVersionInMemoryRepository productsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private UpdateProductUseCase updateProductUseCase;

    @BeforeEach
    void beforeEach() {
        eventInMemoryBus = new EventInMemoryBus();
        productsInMemoryRepository = new ProductVersionInMemoryRepository();

        updateProductUseCase = new UpdateProductUseCase(productsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void should_updateProductVersion_when_exists() {
        var appleV1 = ProductVersionMother.appleV1();

        var newName = LocalizedField.createOf("en-US", "new product name");
        var params = new UpdateProductParams(
                appleV1.getVersionId(),
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
    void should_failedAsUnknownProductVersion_when_notExist() {
        var bananaV1 = ProductVersionMother.bananaV1();

        var newName = LocalizedField.createOf("en-US", "new product name");
        var params = new UpdateProductParams(
                bananaV1.getVersionId(),
                newName
        );

        assertThrows(ProductVersionNotFoundException.class,
                () -> updateProductUseCase.execute(
                    ContextMother.john(),
                    params));
    }

    private void assertProductDataInDatabase(ProductVersion result) {
        assertTrue(productsInMemoryRepository.contains(result));
    }

    private void assertProductUpdatedWasPublished(ProductVersion productVersion) {
        var event = eventInMemoryBus.filterOfClass(ProductVersionUpdated.class)
            .findFirst();

        if( event.isEmpty()) {
            fail();
        }

        assertEquals(productVersion.getVersionId(), event.get().getVersionId());
    }
}
