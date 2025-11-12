package com.jeferro.ecommerce.products.reviews.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.reviews.application.params.DeleteReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    reviewsRepository.delete(review);

    eventBus.sendAll(review);

    return review;
  }
}
