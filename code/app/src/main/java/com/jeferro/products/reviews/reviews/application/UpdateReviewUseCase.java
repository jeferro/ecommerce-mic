package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.UpdateReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class UpdateReviewUseCase extends UseCase<UpdateReviewParams, Review> {

    private final ReviewsRepository reviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Review execute(Context context, UpdateReviewParams params) {
        var review = ensureReviewExists(params);

        review.ensureReviewBelongsToUser(context.getAuth());

        return updateReview(context, params, review);
    }

    private Review ensureReviewExists(UpdateReviewParams params) {
        var reviewId = params.getReviewId();

        return reviewsRepository.findByIdOrError(reviewId);
    }

    private Review updateReview(Context context, UpdateReviewParams params, Review review) {
	    var locale = context.getLocale();

        var comment = params.getComment();

        review.update(comment, locale);

        reviewsRepository.save(review);

        eventBus.sendAll(review);

        return review;
    }
}
