package com.jeferro.ecommerce.products.reviews.domain.repositories;

import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import java.util.List;
import java.util.Optional;

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
