package com.jeferro.ecommerce.products.reviews.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.reviews.application.params.SearchReviewParams;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.utils.FutureUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchReviewUseCase extends UseCase<SearchReviewParams, PaginatedList<Review>> {

  private final ReviewsRepository reviewsRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public PaginatedList<Review> execute(Auth auth, SearchReviewParams params) {
    var criteria = params.getCriteria();

    return FutureUtils.async(
        () -> reviewsRepository.findAll(criteria),
        () -> reviewsRepository.count(criteria),
        (reviews, totalReviews) -> PaginatedList.createOfCriteria(criteria, reviews, totalReviews));
  }
}
