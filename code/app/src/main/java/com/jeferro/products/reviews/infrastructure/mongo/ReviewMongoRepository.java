package com.jeferro.products.reviews.infrastructure.mongo;

import java.util.List;
import java.util.Optional;

import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.products.reviews.infrastructure.mongo.daos.ReviewMongoDao;
import com.jeferro.products.reviews.infrastructure.mongo.mappers.ReviewMongoMapper;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMongoRepository implements ReviewsRepository {

  private final ReviewMongoMapper reviewMongoMapper = ReviewMongoMapper.INSTANCE;

  private final ReviewMongoDao reviewMongoDao;

  @Override
  public void save(Review review) {
    var reviewDto = reviewMongoMapper.toDTO(review);

    reviewMongoDao.save(reviewDto);
  }

  @Override
  public Optional<Review> findById(ReviewId reviewId) {
    var productReviewIdDto = reviewMongoMapper.toDTO(reviewId);

    return reviewMongoDao.findById(productReviewIdDto).map(reviewMongoMapper::toDomain);
  }

  @Override
  public void delete(Review review) {
    var productReviewDto = reviewMongoMapper.toDTO(review);

    reviewMongoDao.delete(productReviewDto);
  }

  @Override
  public void deleteAll(List<Review> reviews) {
    var productReviewIds = reviews.stream()
				.map(Entity::getId)
				.map(reviewMongoMapper::toDTO)
				.toList();

    reviewMongoDao.deleteAllById(productReviewIds);
  }

  @Override
  public List<Review> findAll(ReviewCriteria criteria) {
    var page = reviewMongoDao.findAll(criteria);

    return reviewMongoMapper.toDomain(page);
  }

	@Override
	public long count(ReviewCriteria criteria) {
		return reviewMongoDao.count(criteria);
	}
}
