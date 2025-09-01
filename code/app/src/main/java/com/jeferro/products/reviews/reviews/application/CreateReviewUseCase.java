package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.CreateReviewParams;
import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewAlreadyExistsException;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

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

	return createReview(reviewId, params, auth);
  }

  private void ensureReviewDoesNotExists(ReviewId reviewId) {
	var review = reviewsRepository.findById(reviewId);

	if(review.isPresent()){
	  throw ReviewAlreadyExistsException.createOf(reviewId);
	}
  }

  private Review createReview(ReviewId reviewId, CreateReviewParams params, Auth auth) {
	var review = Review.createOf(reviewId,
		auth.getLocale(),
		params.getComment()
	);

	reviewsRepository.save(review);

	eventBus.sendAll(review);

	return review;
  }
}
