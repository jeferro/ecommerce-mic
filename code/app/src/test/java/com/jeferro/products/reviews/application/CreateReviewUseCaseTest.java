package com.jeferro.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.jeferro.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.reviews.application.params.CreateReviewParams;
import com.jeferro.products.reviews.domain.events.ReviewCreated;
import com.jeferro.products.reviews.domain.exceptions.ReviewAlreadyExistsException;
import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.apple().getValue());
    var comment = "New comment about product";
    var params = new CreateReviewParams(appleEntityId, comment);

    var authJames = AuthMother.james();
    var result = createReviewUseCase.execute(authJames, params);

    assertResult(authJames, result, appleEntityId, comment);

    assertReviewInDatabase(result);

    assertReviewCreatedWasPublished(result);
  }

  @Test
  void should_failedAsReviewExists_when_newReviewExists() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var params = new CreateReviewParams(johnReviewOfApple.getEntityId(), "other comment");

    assertThrows(
        ReviewAlreadyExistsException.class,
        () -> createReviewUseCase.execute(AuthMother.john(), params));
  }

  private static void assertResult(Auth auth, Review result, EntityId entityId, String comment) {
    assertEquals(auth.getUsername(), result.getUsername());
    assertEquals(entityId, result.getEntityId());
    assertEquals(comment, result.getComment());
  }

  private void assertReviewInDatabase(Review result) {
    assertTrue(reviewsInMemoryRepository.contains(result));
  }

  private void assertReviewCreatedWasPublished(Review result) {
    var event = eventInMemoryBus.filterOfClass(ReviewCreated.class).findFirst();

    if (event.isEmpty()) {
      fail();
    }

    assertEquals(result.getId(), event.get().getReviewId());
  }
}
