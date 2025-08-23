package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.SearchProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchProductVersionReviewUseCaseTest {

  private SearchProductReviewUseCase searchProductReviewUseCase;

    @BeforeEach
    public void beforeEach() {
	  ProductReviewsInMemoryRepository productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();

        searchProductReviewUseCase = new SearchProductReviewUseCase(productReviewsInMemoryRepository);
    }

    @Test
    void givenProductReviews_whenListProductReviews_thenReturnsProductReviews() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var params = new SearchProductReviewParams(
                johnReviewOfApple.getProductCode()
        );

        var result = searchProductReviewUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(2, result.size());
        assertTrue(result.contains(johnReviewOfApple));
    }

    @Test
    void givenNoProductReviews_whenListProductReviews_thenReturnsEmptyList() {
        var pearV1 = ProductVersionMother.pearV1();

        var params = new SearchProductReviewParams(
                pearV1.getCode()
        );

        var result = searchProductReviewUseCase.execute(
            ContextMother.john(),
            params);

        assertTrue(result.isEmpty());
    }

}
