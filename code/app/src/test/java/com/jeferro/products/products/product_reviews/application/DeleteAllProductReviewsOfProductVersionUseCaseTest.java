package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.DeleteAllProductReviewsOfProductParams;
import com.jeferro.products.products.product_reviews.domain.events.ProductReviewDeleted;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAllProductReviewsOfProductVersionUseCaseTest {

    private ProductReviewsInMemoryRepository productReviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private DeleteAllProductReviewsOfProductUseCase deleteAllProductReviewsOfProductUseCase;

    @BeforeEach
    public void beforeEach() {
        productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        deleteAllProductReviewsOfProductUseCase =
                new DeleteAllProductReviewsOfProductUseCase(productReviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenSeveralReviewsOfSameProduct_whenDeleteItsReviews_thenPublishEvents() {
        var appleCode = ProductCodeMother.apple();

        var params = new DeleteAllProductReviewsOfProductParams(
                appleCode
        );

        deleteAllProductReviewsOfProductUseCase.execute(
            ContextMother.emily(),
            params);

        assertThereAreNotReviewsOfApple();

        assertProductReviewDeletedWasPublished();
    }

    @Test
    void givenProductDoNotHaveReviews_whenDeleteItsReviews_thenDoNothing() {
        var pearV1 = ProductVersionMother.pearV1();

        var params = new DeleteAllProductReviewsOfProductParams(
                pearV1.getCode()
        );

        deleteAllProductReviewsOfProductUseCase.execute(
            ContextMother.emily(),
            params);

        assertNoEventsWerePublished();
    }

    private void assertThereAreNotReviewsOfApple() {
        assertTrue(productReviewsInMemoryRepository.isEmpty());
    }

    private void assertProductReviewDeletedWasPublished() {
        Set<ProductReviewId> notifiedProductReviewIds = new HashSet<>();

        eventInMemoryBus.forEach(event -> {
            assertInstanceOf(ProductReviewDeleted.class, event);

            ProductReviewDeleted productReviewDeleted = (ProductReviewDeleted) event;
            notifiedProductReviewIds.add(productReviewDeleted.getProductReviewId());
        });

        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        var emilyReviewOfApple = ProductReviewMother.emilyReviewOfApple();
        Set<ProductReviewId> deletedProductReviewIds = Set.of(johnReviewOfApple.getId(),
                emilyReviewOfApple.getId());

        assertEquals(deletedProductReviewIds, notifiedProductReviewIds);
    }

    private void assertNoEventsWerePublished() {
        assertTrue(eventInMemoryBus.isEmpty());
    }

}
