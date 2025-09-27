package com.jeferro.products.reviews.domain.repositories;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.domain.models.ReviewMother;
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
    public PaginatedList<Review> findAll(ReviewCriteria criteria) {
        var entities = data.values().stream()
            .filter(review -> matchCriteria(review, criteria))
            .sorted((r1, r2) -> compareReviews(r1, r2, criteria))
            .toList();

        var paginatedEntities = paginateEntities(entities, criteria);

        return PaginatedList.createOfList(paginatedEntities);
    }

    @Override
    public void deleteAll(PaginatedList<Review> reviews) {
        reviews.stream()
                .map(Entity::getId)
                .forEach(this::deleteById);
    }

    private boolean matchCriteria(Review review, ReviewCriteria criteria) {
        return matchEntityId(review, criteria);
    }

    private boolean matchEntityId(Review review, ReviewCriteria criteria) {
        return !criteria.hasEntityId()
            || review.getEntityId().equals(criteria.getEntityId());
    }

    private int compareReviews(Review r1, Review r2, ReviewCriteria criteria) {
        return switch (criteria.getOrder()) {
            case ID -> -1;
        };
    }
}
