package com.jeferro.products.reviews.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.jeferro.products.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

public interface ReviewsRepository {

  void save(Review review);

  Optional<Review> findById(ReviewId reviewId);

  default Review findByIdOrError(ReviewId reviewId) {
    return findById(reviewId).orElseThrow(() -> ReviewNotFoundException.createOf(reviewId));
  }

  void delete(Review review);

  void deleteAll(List<Review> reviews);

  List<Review> findAll(ReviewCriteria criteria);

  long count(ReviewCriteria criteria);
}
