package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.reviews.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteAllReviewsOfEntityIdUseCaseTest {

    private ReviewsInMemoryRepository reviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private DeleteAllReviewsOfEntityIdUseCase deleteAllReviewsOfEntityIdUseCase;

    @BeforeEach
    public void beforeEach() {
        reviewsInMemoryRepository = new ReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        deleteAllReviewsOfEntityIdUseCase = new DeleteAllReviewsOfEntityIdUseCase(reviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void should_deleteAllReviews_when_productHaveReviews() {
        var appleEntityId = EntityId.createOf(
            "products",
            ProductCodeMother.apple().getValue()
        );

        var params = new DeleteAllReviewsOfEntityIdParams(
                appleEntityId
        );

        deleteAllReviewsOfEntityIdUseCase.execute(
            ContextMother.emily(),
            params);

        assertThereAreNotReviewsOfApple();

        assertReviewDeletedWasPublished();
    }

    @Test
    void should_notDeleteReviews_when_productNotHaveReviews() {
	  var pearEntityId = EntityId.createOf(
            "products",
            ProductCodeMother.pear().getValue()
        );

        var params = new DeleteAllReviewsOfEntityIdParams(
            pearEntityId
        );

        deleteAllReviewsOfEntityIdUseCase.execute(
            ContextMother.emily(),
            params);

        assertNoEventsWerePublished();
    }

    private void assertThereAreNotReviewsOfApple() {
        assertTrue(reviewsInMemoryRepository.isEmpty());
    }

    private void assertReviewDeletedWasPublished() {
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
