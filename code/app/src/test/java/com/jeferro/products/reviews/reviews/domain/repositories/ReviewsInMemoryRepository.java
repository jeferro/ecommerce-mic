package com.jeferro.products.reviews.reviews.domain.repositories;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewFilter;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

public class ReviewsInMemoryRepository extends InMemoryRepository<Review, ReviewId>
        implements ReviewsRepository {

    public ReviewsInMemoryRepository() {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        data.put(johnReviewOfApple.getId(), johnReviewOfApple);

        var emilyReviewOfApple = ReviewMother.emilyReviewOfApple();
        data.put(emilyReviewOfApple.getId(), emilyReviewOfApple);
    }

    @Override
    public PaginatedList<Review> findAll(ReviewFilter filter) {
        var entities = data.values().stream()
            .filter(review -> matchCriteria(review, filter))
            .sorted((r1, r2) -> compareReviews(r1, r2, filter))
            .toList();

        var paginatedEntities = paginateEntities(entities, filter);

        return PaginatedList.createOfList(paginatedEntities);
    }

    @Override
    public void deleteAll(PaginatedList<Review> reviews) {
        reviews.stream()
                .map(Entity::getId)
                .forEach(this::deleteById);
    }

    private boolean matchEntityId(Review review, ReviewFilter filter) {
        return !filter.hasEntityId()
            || review.getEntityId().equals(filter.getEntityId());
    }

    private boolean matchCriteria(Review review, ReviewFilter filter) {
        return matchEntityId(review, filter);
    }

    private int compareReviews(Review r1, Review r2, ReviewFilter filter) {
        return switch (filter.getOrder()) {
            case ID -> -1;
        };
    }
}
