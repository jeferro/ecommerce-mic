package com.jeferro.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeferro.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.reviews.application.params.SearchReviewParams;
import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.products.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchReviewUseCaseTest {

  private SearchReviewUseCase searchReviewUseCase;

  @BeforeEach
  public void beforeEach() {
    ReviewsInMemoryRepository reviewsInMemoryRepository = new ReviewsInMemoryRepository();

    searchReviewUseCase = new SearchReviewUseCase(reviewsInMemoryRepository);
  }

  @Test
  void should_returnListOfReviews_when_exist() {
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.apple().getValue());

    var params = new SearchReviewParams(ReviewCriteria.byEntityId(appleEntityId, 100));

    var result = searchReviewUseCase.execute(AuthMother.john(), params);

    assertFalse(result.isEmpty());
  }

  @Test
  void should_returnEmptyList_when_notExist() {
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.banana().getValue());

    var params = new SearchReviewParams(ReviewCriteria.byEntityId(appleEntityId, 100));

    var result = searchReviewUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isEmpty());
  }
}
