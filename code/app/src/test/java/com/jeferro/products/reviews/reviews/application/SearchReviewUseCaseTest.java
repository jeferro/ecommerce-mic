package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.reviews.reviews.application.params.SearchReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.EntityId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewCriteria;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchReviewUseCaseTest {

  private SearchReviewUseCase searchReviewUseCase;

    @BeforeEach
    public void beforeEach() {
	  ReviewsInMemoryRepository reviewsInMemoryRepository = new ReviewsInMemoryRepository();

        searchReviewUseCase = new SearchReviewUseCase(reviewsInMemoryRepository);
    }

    @Test
    void should_returnListOfReviews_when_exist() {
      var appleEntityId = EntityId.createOf(
          "products",
          ProductCodeMother.apple().getValue()
      );

        var params = new SearchReviewParams(
            ReviewCriteria.byEntityId(appleEntityId)
        );

        var result = searchReviewUseCase.execute(
            AuthMother.john(),
            params);

        assertFalse(result.isEmpty());
    }

    @Test
    void should_returnEmptyList_when_notExist() {
      var appleEntityId = EntityId.createOf(
          "products",
          ProductCodeMother.banana().getValue()
      );

        var params = new SearchReviewParams(
            ReviewCriteria.byEntityId(appleEntityId)
        );

        var result = searchReviewUseCase.execute(
            AuthMother.john(),
            params);

        assertTrue(result.isEmpty());
    }

}
