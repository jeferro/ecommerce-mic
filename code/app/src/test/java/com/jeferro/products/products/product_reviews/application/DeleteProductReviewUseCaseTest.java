package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.DeleteProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.events.ProductReviewDeleted;
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

class DeleteProductReviewUseCaseTest {

    private ProductReviewsInMemoryRepository productReviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private DeleteProductReviewUseCase deleteProductReviewUseCase;

    @BeforeEach
    public void beforeEach() {
        productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();

        eventInMemoryBus = new EventInMemoryBus();

        deleteProductReviewUseCase = new DeleteProductReviewUseCase(productReviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenUserCommentsOnProduct_whenDeleteProductReview_thenReturnsDeletedProductReview() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var params = new DeleteProductReviewParams(
                johnReviewOfApple.getId()
        );

        var result = deleteProductReviewUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(johnReviewOfApple, result);

        assertFalse(productReviewsInMemoryRepository.contains(result));

        assertProductReviewDeletedWasPublished(result);
    }

    @Test
    void givenUserDoesNotCommentOnProduct_whenDeleteProductReview_throwsException() {
        var jamesReviewOfApple = ProductReviewMother.jamesReviewOfApple();
        var params = new DeleteProductReviewParams(
                jamesReviewOfApple.getId()
        );

        assertThrows(ProductReviewNotFoundException.class,
                () -> deleteProductReviewUseCase.execute(
                    ContextMother.james(),
                    params));
    }

    @Test
    void givenOtherUserCommentsOnProduct_whenDeleteProductReviewOfOtherUser_throwsException() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var params = new DeleteProductReviewParams(
                johnReviewOfApple.getId()
        );

        assertThrows(ProductReviewDoesNotBelongUserException.class,
                () -> deleteProductReviewUseCase.execute(
                    ContextMother.emily(),
                    params));
    }

    private void assertProductReviewDeletedWasPublished(ProductReview result) {
        assertEquals(1, eventInMemoryBus.size());

        var event = (ProductReviewDeleted) eventInMemoryBus.getFirstOrError();

        assertEquals(result.getId(), event.getProductReviewId());
    }
}
