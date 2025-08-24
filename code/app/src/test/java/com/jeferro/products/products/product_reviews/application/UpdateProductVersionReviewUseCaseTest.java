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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class UpdateProductVersionReviewUseCaseTest {

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
    void should_updateReview_when_exist() {
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
    void should_failedAsReviewNotFound_when_notExist() {
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
    void should_failedAsDeniedPermission_when_userUpdatesReviewOtherUser() {
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
        var event = eventInMemoryBus.filterOfClass(ProductReviewUpdated.class)
            .findFirst();

        if(event.isEmpty()){
            fail();
        }

        assertEquals(result.getId(), event.get().getProductReviewId());
    }
}
