package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.DeleteReviewParams;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewDoesNotBelongUserException;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteReviewUseCaseTest {

    private ReviewsInMemoryRepository reviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private DeleteReviewUseCase deleteReviewUseCase;

    @BeforeEach
    public void beforeEach() {
        reviewsInMemoryRepository = new ReviewsInMemoryRepository();

        eventInMemoryBus = new EventInMemoryBus();

        deleteReviewUseCase = new DeleteReviewUseCase(reviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void should_deleteReview_when_exists() {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();

        var params = new DeleteReviewParams(
                johnReviewOfApple.getId()
        );

        var result = deleteReviewUseCase.execute(
            AuthMother.john(),
            params);

        assertEquals(johnReviewOfApple, result);

        assertFalse(reviewsInMemoryRepository.contains(result));

        assertReviewDeletedWasPublished(result);
    }

    @Test
    void should_failedAsReviewNotFound_when_notExist() {
        var jamesReviewOfApple = ReviewMother.jamesReviewOfApple();
        var params = new DeleteReviewParams(
                jamesReviewOfApple.getId()
        );

        assertThrows(ReviewNotFoundException.class,
                () -> deleteReviewUseCase.execute(
                    AuthMother.james(),
                    params));
    }

    @Test
    void should_failedAsDeniedPermission_when_userDeletesCommentOfOtherUser() {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();

        var params = new DeleteReviewParams(
                johnReviewOfApple.getId()
        );

        assertThrows(ReviewDoesNotBelongUserException.class,
                () -> deleteReviewUseCase.execute(
                    AuthMother.emily(),
                    params));
    }

    private void assertReviewDeletedWasPublished(Review result) {
        var event = eventInMemoryBus.filterOfClass(ReviewDeleted.class)
            .findFirst();

        if(event.isEmpty()){
            fail();
        }

        assertEquals(result.getId(), event.get().getReviewId());
    }
}
