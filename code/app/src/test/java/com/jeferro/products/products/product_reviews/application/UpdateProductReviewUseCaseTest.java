package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.UpdateProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.events.ProductReviewUpdated;
import com.jeferro.products.products.product_reviews.domain.exceptions.ProductReviewDoesNotBelongUserException;
import com.jeferro.products.products.product_reviews.domain.exceptions.ProductReviewNotFoundException;
import com.jeferro.products.products.product_reviews.domain.models.ProductReview;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProductReviewUseCaseTest {

    private ProductReviewsInMemoryRepository productReviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private UpdateProductReviewUseCase updateProductReviewUseCase;

    @BeforeEach
    void beforeEach() {
        productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        updateProductReviewUseCase = new UpdateProductReviewUseCase(productReviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenAProductReview_whenUpdateProductReview_thenReturnsUpdatedProductReview() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var newComment = "New comment about apple";
        var params = new UpdateProductReviewParams(
                johnReviewOfApple.getId(),
                newComment
        );
        var result = updateProductReviewUseCase.execute(
            ContextMother.john(),
            params);

        assertResult(johnReviewOfApple, result, newComment);

        assertProductReviewInDatabase(result);

        assertProductReviewUpdatedWasPublished(result);
    }

    @Test
    void givenNoProductReview_whenUpdateProductReview_thenThrowsException() {
        var jamesReviewOfApple = ProductReviewMother.jamesReviewOfApple();
        var newComment = "New comment about apple";
        var params = new UpdateProductReviewParams(
                jamesReviewOfApple.getId(),
                newComment
        );

        assertThrows(ProductReviewNotFoundException.class,
                () -> updateProductReviewUseCase.execute(
                    ContextMother.james(),
                    params));
    }

    @Test
    void givenOtherUserCommentsOnProduct_whenUpdateProductReviewOfOtherUser_throwsException() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var newComment = "New comment about apple";
        var params = new UpdateProductReviewParams(
                johnReviewOfApple.getId(),
                newComment
        );

        assertThrows(ProductReviewDoesNotBelongUserException.class,
                () -> updateProductReviewUseCase.execute(
                    ContextMother.emily(),
                    params));
    }

    private static void assertResult(ProductReview userReviewOfApple, ProductReview result, String newComment) {
        assertEquals(userReviewOfApple.getId(), result.getId());
        assertEquals(newComment, result.getComment());
    }

    private void assertProductReviewInDatabase(ProductReview result) {
        assertTrue(productReviewsInMemoryRepository.contains(result));
    }

    private void assertProductReviewUpdatedWasPublished(ProductReview result) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductReviewUpdated) eventInMemoryBus.getFirstOrError();

        assertEquals(result.getId(), event.getProductReviewId());
    }
}
