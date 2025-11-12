package com.jeferro.ecommerce.products.reviews.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.ADMIN;

import com.jeferro.ecommerce.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.ecommerce.shared.domain.utils.PageUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllReviewsOfEntityIdUseCase
    extends UseCase<DeleteAllReviewsOfEntityIdParams, List<ReviewId>> {

  private static final int PAGE_SIZE = 100;

  private final ReviewsRepository reviewsRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(ADMIN);
  }

  @Override
  public List<ReviewId> execute(Auth auth, DeleteAllReviewsOfEntityIdParams params) {
    var entityId = params.getEntityId();
    var criteria = ReviewCriteria.byEntityId(entityId, PAGE_SIZE);

    var totalReviews = reviewsRepository.count(criteria);
    var totalPages = PageUtils.calculateTotalPages(totalReviews, PAGE_SIZE);

    var removedReviewIds = new ArrayList<ReviewId>();

    for (int i = 0; i < totalPages; i++) {
      var reviews = reviewsRepository.findAll(criteria);

      reviews.forEach(
          review -> {
            review.deleteBySystem();
            removedReviewIds.add(review.getId());
          });

      reviewsRepository.deleteAll(reviews);

      eventBus.sendAll(reviews);

      criteria.nextPage();
    }

    return removedReviewIds;
  }
}
