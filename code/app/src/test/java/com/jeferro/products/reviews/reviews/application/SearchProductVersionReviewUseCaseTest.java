package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.SearchProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsInMemoryRepository;
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
    void should_returnListOfReviews_when_exist() {
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
    void should_returnEmptyList_when_notExist() {
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
