package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.reviews.reviews.application.params.CreateReviewParams;
import com.jeferro.products.reviews.reviews.domain.events.ReviewCreated;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewAlreadyExistsException;
import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CreateReviewUseCaseTest {

  private ReviewsInMemoryRepository reviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private CreateReviewUseCase createReviewUseCase;

    @BeforeEach
    public void beforeEach() {
        reviewsInMemoryRepository = new ReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        createReviewUseCase = new CreateReviewUseCase(reviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void should_createReview_when_reviewNotExist() {
        var appleEntityId = EntityId.createOf(
            "products",
            ProductCodeMother.apple().getValue()
        );
        var comment = "New comment about product";
        var params = new CreateReviewParams(
                appleEntityId,
                comment
        );

        var jamesContext = ContextMother.james();
        var result = createReviewUseCase.execute(jamesContext, params);

        assertResult(jamesContext, result, appleEntityId, comment);

        assertReviewInDatabase(result);

        assertReviewCreatedWasPublished(result);
    }

    @Test
    void should_failedAsReviewExists_when_newReviewExists() {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();

        var params = new CreateReviewParams(
                johnReviewOfApple.getEntityId(),
                "other comment"
        );

        assertThrows(ReviewAlreadyExistsException.class,
                () -> createReviewUseCase.execute(
                    ContextMother.john(),
                    params));
    }

    private static void assertResult(Context context, Review result, EntityId entityId, String comment) {
        assertEquals(context.getAuth().username(), result.getUsername());
        assertEquals(entityId, result.getEntityId());
        assertEquals(comment, result.getComment());
    }

    private void assertReviewInDatabase(Review result) {
        assertTrue(reviewsInMemoryRepository.contains(result));
    }

    private void assertReviewCreatedWasPublished(Review result) {
        var event = eventInMemoryBus.filterOfClass(ReviewCreated.class)
            .findFirst();

        if(event.isEmpty()){
          fail();
        }

        assertEquals(result.getId(), event.get().getReviewId());
    }
}
