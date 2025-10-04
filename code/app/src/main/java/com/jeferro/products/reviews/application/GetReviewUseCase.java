package com.jeferro.products.reviews.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.reviews.application.params.GetReviewParams;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetReviewUseCase extends UseCase<GetReviewParams, Review> {

  private final ReviewsRepository reviewsRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public Review execute(Auth auth, GetReviewParams params) {
    var reviewId = params.getReviewId();

    return reviewsRepository.findByIdOrError(reviewId);
  }
}
