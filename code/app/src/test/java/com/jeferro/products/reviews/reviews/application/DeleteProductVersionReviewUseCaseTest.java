package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.DeleteProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.exceptions.ProductReviewDoesNotBelongUserException;
import com.jeferro.products.reviews.reviews.domain.exceptions.ProductReviewNotFoundException;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteProductVersionReviewUseCaseTest {

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
    void should_deleteReview_when_exists() {
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
    void should_failedAsReviewNotFound_when_notExist() {
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
    void should_failedAsDeniedPermission_when_userDeletesCommentOfOtherUser() {
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
        var event = eventInMemoryBus.filterOfClass(ProductReviewDeleted.class)
            .findFirst();

        if(event.isEmpty()){
            fail();
        }

        assertEquals(result.getId(), event.get().getProductReviewId());
    }
}
