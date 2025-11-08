package com.jeferro.products.reviews.application;

import static com.jeferro.products.shared.application.Roles.ADMIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jeferro.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.products.shared.domain.utils.PageUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllReviewsOfEntityIdUseCase extends UseCase<DeleteAllReviewsOfEntityIdParams, List<ReviewId>> {

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

		for(int i = 0; i < totalPages; i++){
			var reviews = reviewsRepository.findAll(criteria);

			reviews.forEach(review -> {
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
