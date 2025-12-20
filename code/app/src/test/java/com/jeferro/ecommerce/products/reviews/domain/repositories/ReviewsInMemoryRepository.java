package com.jeferro.ecommerce.products.reviews.domain.repositories;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewMother;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.shared.domain.repositories.InMemoryRepository;
import java.util.List;

public class ReviewsInMemoryRepository extends InMemoryRepository<Review, ReviewId>
    implements ReviewsRepository {

  public ReviewsInMemoryRepository() {
    var johnReviewOfApple = ReviewMother.johnReviewOfApple();
    data.put(johnReviewOfApple.getId(), johnReviewOfApple);

    var emilyReviewOfApple = ReviewMother.emilyReviewOfApple();
    data.put(emilyReviewOfApple.getId(), emilyReviewOfApple);
  }

  @Override
  public List<Review> findAll(ReviewCriteria criteria) {
    var entities =
        data.values().stream()
            .filter(review -> matchCriteria(review, criteria))
            .sorted((r1, r2) -> compare(r1, r2, criteria))
            .toList();

    return paginateEntities(entities, criteria);
  }

  @Override
  public long count(ReviewCriteria criteria) {
    return findAll(criteria).size();
  }

  @Override
  public void deleteAll(List<Review> reviews) {
    reviews.forEach(this::delete);
  }

  private boolean matchCriteria(Review review, ReviewCriteria criteria) {
    return matchEntityId(review, criteria);
  }

  private boolean matchEntityId(Review review, ReviewCriteria criteria) {
    return !criteria.hasEntityId() || review.getEntityId().equals(criteria.getEntityId());
  }

  private int compare(Review r1, Review r2, ReviewCriteria criteria) {
    var order = criteria.getOrder();

    if(order == null){
      return -1;
    }

    return switch (criteria.getOrder()) {
	  default -> -1;
	};
  }
}
