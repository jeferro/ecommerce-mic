package com.jeferro.products.reviews.reviews.domain.repositories;

import com.jeferro.products.reviews.reviews.domain.exceptions.ReviewNotFoundException;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewCriteria;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.Optional;

public interface ReviewsRepository {

    void save(Review review);

    Optional<Review> findById(ReviewId reviewId);

    default Review findByIdOrError(ReviewId reviewId) {
        return findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.createOf(reviewId));
    }

    void deleteById(ReviewId reviewId);

    void deleteAll(PaginatedList<Review> reviews);

    PaginatedList<Review> findAll(ReviewCriteria criteria);
}
