package com.jeferro.ecommerce.products.reviews.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.reviews.application.params.CreateReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewAlreadyExistsException;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateReviewUseCase extends UseCase<CreateReviewParams, Review> {

  private final ReviewsRepository reviewsRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public Review execute(Auth auth, CreateReviewParams params) {
    ReviewId reviewId = ReviewId.createOf(params.getEntityId(), auth);

    ensureReviewDoesNotExists(reviewId);

    return createReview(params, auth);
  }

  private void ensureReviewDoesNotExists(ReviewId reviewId) {
    var review = reviewsRepository.findById(reviewId);

    if (review.isPresent()) {
      throw ReviewAlreadyExistsException.createOf(reviewId);
    }
  }

  private Review createReview(CreateReviewParams params, Auth auth) {
    var review = Review.createOf(params.getEntityId(), params.getComment(), auth);

    reviewsRepository.save(review);

    eventBus.sendAll(review);

    return review;
  }
}
