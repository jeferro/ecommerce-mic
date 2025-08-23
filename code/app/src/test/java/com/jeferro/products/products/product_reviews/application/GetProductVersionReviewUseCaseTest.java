package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.GetProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.exceptions.ProductReviewNotFoundException;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetProductVersionReviewUseCaseTest {

  private GetProductReviewUseCase getProductReviewUseCase;

    @BeforeEach
    void beforeEach() {
	  ProductReviewsInMemoryRepository productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();

        getProductReviewUseCase = new GetProductReviewUseCase(productReviewsInMemoryRepository);
    }

    @Test
    void givenAProductReview_whenGetProductReview_thenReturnsProductReview() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var params = new GetProductReviewParams(
                johnReviewOfApple.getId()
        );

        var result = getProductReviewUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(johnReviewOfApple, result);
    }

    @Test
    void givenNoProductReview_whenGetProductReview_thenThrowsException() {
        var jamesReviewOfApple = ProductReviewMother.jamesReviewOfApple();
        var params = new GetProductReviewParams(
                jamesReviewOfApple.getId()
        );

        assertThrows(ProductReviewNotFoundException.class,
                () -> getProductReviewUseCase.execute(
                    ContextMother.james(),
                        params));
    }
}
