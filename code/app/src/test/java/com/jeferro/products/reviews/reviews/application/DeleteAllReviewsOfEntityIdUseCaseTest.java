package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAllReviewsOfEntityIdUseCaseTest {

    private ReviewsInMemoryRepository productReviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private DeleteAllReviewsOfEntityIdUseCase deleteAllReviewsOfEntityIdUseCase;

    @BeforeEach
    public void beforeEach() {
        productReviewsInMemoryRepository = new ReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        deleteAllReviewsOfEntityIdUseCase =
                new DeleteAllReviewsOfEntityIdUseCase(productReviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void should_deleteAllReviews_when_productHaveReviews() {
        var appleCode = ProductCodeMother.apple();

        var params = new DeleteAllReviewsOfEntityIdParams(
                appleCode
        );

        deleteAllReviewsOfEntityIdUseCase.execute(
            ContextMother.emily(),
            params);

        assertThereAreNotReviewsOfApple();

        assertProductReviewDeletedWasPublished();
    }

    @Test
    void should_notDeleteReviews_when_productNotHaveReviews() {
        var pearV1 = ProductVersionMother.pearV1();

        var params = new DeleteAllReviewsOfEntityIdParams(
                pearV1.getCode()
        );

        deleteAllReviewsOfEntityIdUseCase.execute(
            ContextMother.emily(),
            params);

        assertNoEventsWerePublished();
    }

    private void assertThereAreNotReviewsOfApple() {
        assertTrue(productReviewsInMemoryRepository.isEmpty());
    }

    private void assertProductReviewDeletedWasPublished() {
        Set<ReviewId> notifiedReviewIds = new HashSet<>();

        eventInMemoryBus.forEach(event -> {
            assertInstanceOf(ReviewDeleted.class, event);

            ReviewDeleted reviewDeleted = (ReviewDeleted) event;
            notifiedReviewIds.add(reviewDeleted.getReviewId());
        });

        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        var emilyReviewOfApple = ReviewMother.emilyReviewOfApple();
        Set<ReviewId> deletedReviewIds = Set.of(johnReviewOfApple.getId(),
                emilyReviewOfApple.getId());

        assertEquals(deletedReviewIds, notifiedReviewIds);
    }

    private void assertNoEventsWerePublished() {
        assertTrue(eventInMemoryBus.isEmpty());
    }

}
