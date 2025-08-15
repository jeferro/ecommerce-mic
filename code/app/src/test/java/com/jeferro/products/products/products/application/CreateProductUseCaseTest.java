package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.services.ParametricInMemoryFinder;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.products.domain.events.ProductCreated;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        var apple = ProductMother.apple();

        var params = new CreateProductParams(
            apple.getId(),
            apple.getTypeId(),
            apple.getName());

        var result = createProductUseCase.execute(
            ContextMother.user(),
            params);

        assertEquals(apple, result);

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

        assertEquals(result.getId(), event.getId());
    }
}
