package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.services.ParametricInMemoryFinder;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.products.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.products.domain.events.ProductCreated;
import com.jeferro.products.products.products.domain.events.ProductUpdated;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void should_createVersion_when_versionNotExists() {
        var pearV2 = pearV2();

        var params = new CreateProductParams(
            pearV2.getId(),
            pearV2.getTypeId(),
            pearV2.getName());

        var result = createProductUseCase.execute(
            ContextMother.john(),
            params);

        assertNull(result.getEndEffectiveDate());

        assertTrue(productsInMemoryRepository.contains(result));

        assertProductCreatedWasPublished(result);
    }

    @Test
    void should_updatePreviousVersion_when_previousVersionExists() {
        var pearV2 = pearV2();

        var params = new CreateProductParams(
            pearV2.getId(),
            pearV2.getTypeId(),
            pearV2.getName());

        createProductUseCase.execute(
            ContextMother.john(),
            params);

        var pearV1Id = ProductMother.pearV1().getId();
        var pearV1 = productsInMemoryRepository.findByIdOrError(pearV1Id);

        assertEquals(pearV2.getEffectiveDate().minus(1, ChronoUnit.SECONDS), pearV1.getEndEffectiveDate());

        assertProductUpdatedWasPublished(pearV1);
    }

    @Test
    void should_setEndEffectiveDate_when_nextVersionExists() {
        var previousPearV2 = previousPearV2();

        var params = new CreateProductParams(
            previousPearV2.getId(),
            previousPearV2.getTypeId(),
            previousPearV2.getName());

        var result = createProductUseCase.execute(
            ContextMother.john(),
            params);

        var pearV1Id = ProductMother.pearV1().getId();
        var pearV1 = productsInMemoryRepository.findByIdOrError(pearV1Id);

        assertEquals(pearV1.getEffectiveDate().minus(1, ChronoUnit.SECONDS), result.getEndEffectiveDate());

        assertTrue(productsInMemoryRepository.contains(result));
    }

    @Test
    void should_failedAsDuplicatedVersion_when_versionExists() {
        var pearV1 = ProductMother.pearV1();

        var params = new CreateProductParams(
            pearV1.getId(),
            pearV1.getTypeId(),
            pearV1.getName());

        assertThrows(ProductVersionAlreadyExistsException.class,
            () -> createProductUseCase.execute(
                ContextMother.john(),
                params));
    }

    private void assertProductCreatedWasPublished(Product result) {
        assertFalse(eventInMemoryBus.isEmpty());

        var event = (ProductCreated) eventInMemoryBus.getOrError(1);

        assertEquals(result.getId(), event.getId());
    }

    private void assertProductUpdatedWasPublished(Product previous) {
        assertFalse(eventInMemoryBus.isEmpty());

        var event = (ProductUpdated) eventInMemoryBus.getOrError(0);

        assertEquals(previous.getId(), event.getId());
    }

    public Product pearV2() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = Instant.parse("2025-02-01T09:00:00.00Z");
        var productId = ProductId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

        return new Product(productId, name, fruitId, null, PUBLISHED);
    }

    public Product previousPearV2() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = ProductMother.pearV1().getEffectiveDate().minus(60, ChronoUnit.DAYS);
        var productId = ProductId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

        return new Product(productId, name, fruitId, null, PUBLISHED);
    }
}
