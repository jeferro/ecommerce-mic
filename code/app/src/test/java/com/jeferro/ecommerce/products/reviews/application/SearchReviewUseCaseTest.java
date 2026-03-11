package com.jeferro.ecommerce.products.reviews.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCodeMother;
import com.jeferro.ecommerce.products.reviews.application.params.SearchReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsFakeRepository;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchReviewUseCaseTest {

  private SearchReviewUseCase searchReviewUseCase;

  @BeforeEach
  public void beforeEach() {
    ReviewsFakeRepository reviewsInMemoryRepository = new ReviewsFakeRepository();

    searchReviewUseCase = new SearchReviewUseCase(reviewsInMemoryRepository);
  }

  @Test
  void should_returnListOfReviews_when_exist() {
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.apple().toString());

    var params = new SearchReviewParams(ReviewCriteria.allByEntityId(appleEntityId));

    var result = searchReviewUseCase.execute(AuthMother.john(), params);

    assertFalse(result.isEmpty());
  }

  @Test
  void should_returnEmptyList_when_notExist() {
    var appleEntityId = EntityId.createOf("products", ProductCodeMother.banana().toString());

    var params = new SearchReviewParams(ReviewCriteria.allByEntityId(appleEntityId));

    var result = searchReviewUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isEmpty());
  }
}
