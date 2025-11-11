package com.jeferro.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.products.reviews.application.params.UpdateReviewParams;
import com.jeferro.products.reviews.domain.events.ReviewUpdated;
import com.jeferro.products.reviews.domain.exceptions.ReviewDoesNotBelongUserException;
import com.jeferro.products.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateReviewUseCaseTest {

  private ReviewsInMemoryRepository reviewsInMemoryRepository;

  private EventInMemoryBus eventInMemoryBus;

  private UpdateReviewUseCase updateReviewUseCase;

  @BeforeEach
  void beforeEach() {
    reviewsInMemoryRepository = new ReviewsInMemoryRepository();
    eventInMemoryBus = new EventInMemoryBus();

    updateReviewUseCase = new UpdateReviewUseCase(reviewsInMemoryRepository, eventInMemoryBus);
  }

  @Test
  void should_updateReview_when_exist() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(johnReviewOfApple.getId(), newComment);
    var result = updateReviewUseCase.execute(AuthMother.john(), params);

    assertResult(johnReviewOfApple, result, newComment);

    assertReviewInDatabase(result);

    assertReviewUpdatedWasPublished(result);
  }

  @Test
  void should_failedAsReviewNotFound_when_notExist() {
    var jamesReviewOfApple = ReviewMother.jamesReviewOfApple();
    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(jamesReviewOfApple.getId(), newComment);

    assertThrows(
        ReviewNotFoundException.class,
        () -> updateReviewUseCase.execute(AuthMother.james(), params));
  }

  @Test
  void should_failedAsDeniedPermission_when_userUpdatesReviewOtherUser() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(johnReviewOfApple.getId(), newComment);

    assertThrows(
        ReviewDoesNotBelongUserException.class,
        () -> updateReviewUseCase.execute(AuthMother.emily(), params));
  }

  private static void assertResult(Review userReviewOfApple, Review result, String newComment) {
    assertEquals(userReviewOfApple.getId(), result.getId());
    assertEquals(newComment, result.getComment());
  }

  private void assertReviewInDatabase(Review result) {
    assertTrue(reviewsInMemoryRepository.contains(result));
  }

  private void assertReviewUpdatedWasPublished(Review result) {
    var event = eventInMemoryBus.filterOfClass(ReviewUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(result.getId(), event.get().getEntityId());
  }
}
