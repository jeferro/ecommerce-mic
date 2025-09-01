package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.GetReviewParams;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsInMemoryRepository;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetReviewUseCaseTest {

  private GetReviewUseCase getReviewUseCase;

    @BeforeEach
    void beforeEach() {
	  ReviewsInMemoryRepository reviewsInMemoryRepository = new ReviewsInMemoryRepository();

        getReviewUseCase = new GetReviewUseCase(reviewsInMemoryRepository);
    }

    @Test
    void should_returnReview_when_exists() {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();

        var params = new GetReviewParams(
                johnReviewOfApple.getId()
        );

        var result = getReviewUseCase.execute(
            AuthMother.john(),
            params);

        assertEquals(johnReviewOfApple, result);
    }

    @Test
    void should_failedAsReviewNotFound_when_notExist() {
        var jamesReviewOfApple = ReviewMother.jamesReviewOfApple();
        var params = new GetReviewParams(
                jamesReviewOfApple.getId()
        );

        assertThrows(ReviewNotFoundException.class,
                () -> getReviewUseCase.execute(
                    AuthMother.james(),
                    params));
    }
}
