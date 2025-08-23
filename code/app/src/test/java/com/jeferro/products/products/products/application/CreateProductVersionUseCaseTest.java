package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.services.ParametricInMemoryFinder;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.products.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.products.domain.events.ProductVersionCreated;
import com.jeferro.products.products.products.domain.events.ProductVersionUpdated;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CreateProductVersionUseCaseTest {

    private ProductVersionInMemoryRepository productsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    void beforeEach() {
        eventInMemoryBus = new EventInMemoryBus();
        productsInMemoryRepository = new ProductVersionInMemoryRepository();

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
            pearV2.getVersionId(),
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
    void should_setEndEffectiveDateOfPreviousVersion_when_previousVersionExists() {
        var pearV2 = pearV2();

        var params = new CreateProductParams(
            pearV2.getVersionId(),
            pearV2.getTypeId(),
            pearV2.getName());

        createProductUseCase.execute(
            ContextMother.john(),
            params);

        var pearV1Id = ProductVersionMother.pearV1().getVersionId();
        var pearV1 = productsInMemoryRepository.findByIdOrError(pearV1Id);

        assertEquals(pearV2.getEffectiveDate().minus(1, ChronoUnit.SECONDS), pearV1.getEndEffectiveDate());

        assertProductUpdatedWasPublished(pearV1);
    }

    @Test
    void should_setEndEffectiveDate_when_nextVersionExists() {
        var previousPearV2 = previousPearV2();

        var params = new CreateProductParams(
            previousPearV2.getVersionId(),
            previousPearV2.getTypeId(),
            previousPearV2.getName());

        var result = createProductUseCase.execute(
            ContextMother.john(),
            params);

        assertTrue(productsInMemoryRepository.contains(result));

        assertEndEffectiveDateOfPreviousVersion(result);
    }

    @Test
    void should_failedAsDuplicatedVersion_when_versionExists() {
        var pearV1 = ProductVersionMother.pearV1();

        var params = new CreateProductParams(
            pearV1.getVersionId(),
            pearV1.getTypeId(),
            pearV1.getName());

        assertThrows(ProductVersionAlreadyExistsException.class,
            () -> createProductUseCase.execute(
                ContextMother.john(),
                params));
    }

    private void assertProductCreatedWasPublished(ProductVersion result) {
        var event = eventInMemoryBus.filterOfClass(ProductVersionCreated.class)
            .findFirst();

        if( event.isEmpty()) {
            fail();
        }

        assertEquals(result.getVersionId(), event.get().getVersionId());
    }

    private void assertProductUpdatedWasPublished(ProductVersion previous) {
        var event = eventInMemoryBus.filterOfClass(ProductVersionUpdated.class)
            .findFirst();

        if( event.isEmpty()) {
            fail();
        }

        assertEquals(previous.getVersionId(), event.get().getVersionId());
    }

    private void assertEndEffectiveDateOfPreviousVersion(ProductVersion result) {
        var pearV1Id = ProductVersionMother.pearV1().getVersionId();
        var pearV1 = productsInMemoryRepository.findByIdOrError(pearV1Id);

        assertEquals(pearV1.getEffectiveDate().minus(1, ChronoUnit.SECONDS), result.getEndEffectiveDate());
    }

    public ProductVersion pearV2() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = Instant.parse("2025-02-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

        return new ProductVersion(productId, name, fruitId, null, PUBLISHED);
    }

    public ProductVersion previousPearV2() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = ProductVersionMother.pearV1().getEffectiveDate().minus(60, ChronoUnit.DAYS);
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Pear V2",
            "es-ES", "Pera V2");

        return new ProductVersion(productId, name, fruitId, null, PUBLISHED);
    }
}
