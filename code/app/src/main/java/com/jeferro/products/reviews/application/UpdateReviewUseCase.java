package com.jeferro.products.reviews.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.reviews.application.params.UpdateReviewParams;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
  public Review execute(Auth auth, UpdateReviewParams params) {
    var review = ensureReviewExists(params);

    review.ensureReviewBelongsToUser(auth);

    return updateReview(auth, params, review);
  }

  private Review ensureReviewExists(UpdateReviewParams params) {
    var reviewId = params.getReviewId();

    return reviewsRepository.findByIdOrError(reviewId);
  }

  private Review updateReview(Auth auth, UpdateReviewParams params, Review review) {
    var comment = params.getComment();

    review.update(comment, auth);

    reviewsRepository.save(review);

    eventBus.sendAll(review);

    return review;
  }
}
