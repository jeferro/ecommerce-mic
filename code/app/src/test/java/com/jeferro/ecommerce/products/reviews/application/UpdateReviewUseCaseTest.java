package com.jeferro.ecommerce.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.ecommerce.products.reviews.application.params.UpdateReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewUpdated;
import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewDoesNotBelongUserException;
import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewMother;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsFakeRepository;
import com.jeferro.ecommerce.shared.domain.events.EventFakeBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateReviewUseCaseTest {

  private ReviewsFakeRepository reviewsFakeRepository;

  private EventFakeBus eventFakeBus;

  private UpdateReviewUseCase updateReviewUseCase;

  @BeforeEach
  void beforeEach() {
    reviewsFakeRepository = new ReviewsFakeRepository();
    eventFakeBus = new EventFakeBus();

    updateReviewUseCase = new UpdateReviewUseCase(reviewsFakeRepository, eventFakeBus);
  }

  @Test
  void should_updateReview_when_exist() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(johnReviewOfApple.getId(), newComment, johnReviewOfApple.getVersion());
    var result = updateReviewUseCase.execute(AuthMother.john(), params);

    assertResult(johnReviewOfApple, result, newComment);

    assertReviewInDatabase(result);

    assertReviewUpdatedWasPublished(result);
  }

  @Test
  void should_failedAsReviewNotFound_when_notExist() {
    var jamesReviewOfApple = ReviewMother.jamesReviewOfApple();
    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(jamesReviewOfApple.getId(), newComment, jamesReviewOfApple.getVersion());

    assertThrows(
        ReviewNotFoundException.class,
        () -> updateReviewUseCase.execute(AuthMother.james(), params));
  }

  @Test
  void should_failedAsDeniedPermission_when_userUpdatesReviewOtherUser() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var newComment = "New comment about apple";
    var params = new UpdateReviewParams(johnReviewOfApple.getId(), newComment, johnReviewOfApple.getVersion());

    assertThrows(
        ReviewDoesNotBelongUserException.class,
        () -> updateReviewUseCase.execute(AuthMother.emily(), params));
  }

  private static void assertResult(Review userReviewOfApple, Review result, String newComment) {
    assertEquals(userReviewOfApple.getId(), result.getId());
    assertEquals(newComment, result.getComment());
  }

  private void assertReviewInDatabase(Review result) {
    assertTrue(reviewsFakeRepository.contains(result));
  }

  private void assertReviewUpdatedWasPublished(Review result) {
    var event = eventFakeBus.filterOfClass(ReviewUpdated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(result.getId(), event.get().getEntityId());
  }
}
