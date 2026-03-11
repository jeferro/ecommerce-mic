package com.jeferro.ecommerce.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCodeMother;
import com.jeferro.ecommerce.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewDeleted;
import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewMother;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsFakeRepository;
import com.jeferro.ecommerce.shared.domain.events.EventFakeBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteAllReviewsOfEntityIdUseCaseTest {

  private ReviewsFakeRepository reviewsFakeRepository;

  private EventFakeBus eventFakeBus;

  private DeleteAllReviewsOfEntityIdUseCase deleteAllReviewsOfEntityIdUseCase;

  @BeforeEach
  public void beforeEach() {
    reviewsFakeRepository = new ReviewsFakeRepository();
    eventFakeBus = new EventFakeBus();

    deleteAllReviewsOfEntityIdUseCase =
        new DeleteAllReviewsOfEntityIdUseCase(reviewsFakeRepository, eventFakeBus);
  }

  @Test
  void should_deleteAllReviews_when_productHaveReviews() {
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.apple().toString());

    var params = new DeleteAllReviewsOfEntityIdParams(appleEntityId);

    deleteAllReviewsOfEntityIdUseCase.execute(AuthMother.emily(), params);

    assertThereAreNotReviewsOfApple();

    assertReviewDeletedWasPublished();
  }

  @Test
  void should_notDeleteReviews_when_productNotHaveReviews() {
    var pearEntityId = EntityId.createOf("products", ProductCodeMother.pear().toString());

    var params = new DeleteAllReviewsOfEntityIdParams(pearEntityId);

    deleteAllReviewsOfEntityIdUseCase.execute(AuthMother.emily(), params);

    assertNoEventsWerePublished();
  }

  private void assertThereAreNotReviewsOfApple() {
    assertTrue(reviewsFakeRepository.isEmpty());
  }

  private void assertReviewDeletedWasPublished() {
    Set<ReviewId> notifiedReviewIds = new HashSet<>();

    eventFakeBus.forEach(
        event -> {
          assertInstanceOf(ReviewDeleted.class, event);

          ReviewDeleted reviewDeleted = (ReviewDeleted) event;
          notifiedReviewIds.add(reviewDeleted.getEntityId());
        });

    var johnReviewOfApple = ReviewMother.johnReviewOfApple();
    var emilyReviewOfApple = ReviewMother.emilyReviewOfApple();
    Set<ReviewId> deletedReviewIds = Set.of(johnReviewOfApple.getId(), emilyReviewOfApple.getId());

    assertEquals(deletedReviewIds, notifiedReviewIds);
  }

  private void assertNoEventsWerePublished() {
    assertTrue(eventFakeBus.isEmpty());
  }
}
