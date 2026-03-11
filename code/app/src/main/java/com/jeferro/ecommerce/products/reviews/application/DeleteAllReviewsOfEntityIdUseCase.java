package com.jeferro.ecommerce.products.reviews.application;

import com.jeferro.ecommerce.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.ecommerce.shared.domain.utils.PageUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.IntStream;

import static com.jeferro.ecommerce.shared.domain.models.Roles.ADMIN;

@Component
@RequiredArgsConstructor
public class DeleteAllReviewsOfEntityIdUseCase extends UseCase<DeleteAllReviewsOfEntityIdParams, Void> {

  private static final int PAGE_SIZE = 100;

  private final ReviewsRepository reviewsRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(ADMIN);
  }

  @Override
  public Void execute(Auth auth, DeleteAllReviewsOfEntityIdParams params) {
    var totalPages = calculateTotalPages(params);

    IntStream.range(0, totalPages)
        .parallel()
        .forEach(pageNumber -> deleteReviewsOnPage(params, pageNumber));

    return null;
  }

  private int calculateTotalPages(DeleteAllReviewsOfEntityIdParams params) {
    var totalReviews = reviewsRepository.countAllByEntity(params.getEntityId());

	return PageUtils.calculateTotalPages(totalReviews, PAGE_SIZE);
  }

  private void deleteReviewsOnPage(DeleteAllReviewsOfEntityIdParams params, int pageNumber) {
    var byEntityIdPageCriteria = ReviewCriteria.byEntityIdPage(
        params.getEntityId(),
        pageNumber,
        PAGE_SIZE);

    var reviews = reviewsRepository.findAll(byEntityIdPageCriteria);

    reviews.forEach(Review::deleteBySystem);

    reviewsRepository.deleteAll(reviews);

    eventBus.sendAll(reviews);
  }
}
