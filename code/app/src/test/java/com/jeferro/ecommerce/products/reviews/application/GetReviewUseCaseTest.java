package com.jeferro.ecommerce.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jeferro.ecommerce.products.reviews.application.params.GetReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewMother;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsFakeRepository;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetReviewUseCaseTest {

  private GetReviewUseCase getReviewUseCase;

  @BeforeEach
  void beforeEach() {
    ReviewsFakeRepository reviewsInMemoryRepository = new ReviewsFakeRepository();

    getReviewUseCase = new GetReviewUseCase(reviewsInMemoryRepository);
  }

  @Test
  void should_returnReview_when_exists() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();

    var params = new GetReviewParams(johnReviewOfApple.getId());

    var result = getReviewUseCase.execute(AuthMother.john(), params);

    assertEquals(johnReviewOfApple, result);
  }

  @Test
  void should_failedAsReviewNotFound_when_notExist() {
    var jamesReviewOfApple = ReviewMother.jamesReviewOfApple();
    var params = new GetReviewParams(jamesReviewOfApple.getId());

    assertThrows(
        ReviewNotFoundException.class, () -> getReviewUseCase.execute(AuthMother.james(), params));
  }
}
