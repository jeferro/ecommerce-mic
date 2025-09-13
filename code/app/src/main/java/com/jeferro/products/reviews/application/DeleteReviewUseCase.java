package com.jeferro.products.reviews.application;

import com.jeferro.products.reviews.application.params.DeleteReviewParams;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class DeleteReviewUseCase extends UseCase<DeleteReviewParams, Review> {

    private final ReviewsRepository reviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Review execute(Auth auth, DeleteReviewParams params) {
        var review = ensureReviewExists(params);

        review.ensureReviewBelongsToUser(auth);

        return deleteReview(review);
    }

    private Review ensureReviewExists(DeleteReviewParams params) {
        var reviewId = params.getReviewId();

        return reviewsRepository.findByIdOrError(reviewId);
    }

    private Review deleteReview(Review review) {
	    review.deleteByUser();

        reviewsRepository.deleteById(review.getId());

        eventBus.sendAll(review);

        return review;
    }
}
