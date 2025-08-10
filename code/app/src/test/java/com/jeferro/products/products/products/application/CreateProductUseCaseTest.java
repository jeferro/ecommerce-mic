package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.services.ParametricInMemoryFinder;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.products.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.products.domain.events.ProductCreated;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateProductUseCaseTest {

    private ProductsInMemoryRepository productsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    void beforeEach() {
        eventInMemoryBus = new EventInMemoryBus();
        productsInMemoryRepository = new ProductsInMemoryRepository();

        var parametricFinder = new ParametricInMemoryFinder();
        var parametricValidator = new ParametricValidator(parametricFinder);

        createProductUseCase = new CreateProductUseCase(productsInMemoryRepository,
                parametricValidator,
                eventInMemoryBus);
    }

    @Test
    void givenNoProduct_whenCreateProduct_thenCreatesProduct() {
        var productCode = ProductCodeMother.appleCode();
        var effectiveDate = Instant.now();
        var fruit = ProductTypeMother.fruit();

        var userContext = ContextMother.user();
        var name = LocalizedField.createOf("en-US", "Apple");
        var params = new CreateProductParams(
            productCode,
            effectiveDate,
            fruit.getId(),
            name);

        var result = createProductUseCase.execute(userContext, params);

        assertEquals(productCode, result.getProductCode());
        assertEquals(effectiveDate, result.getEffectiveDate());
        assertEquals(name, result.getName());
        assertEquals(fruit.getId(), result.getTypeId());

        assertProductDataInDatabase(result);

        assertProductCreatedWasPublished(result);
    }

    private void assertProductDataInDatabase(Product result) {
        assertEquals(1, productsInMemoryRepository.size());
        assertTrue(productsInMemoryRepository.contains(result));
    }

    private void assertProductCreatedWasPublished(Product result) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductCreated) eventInMemoryBus.getFirstOrError();

        assertEquals(result.getId(), event.getProductId());
    }
}
