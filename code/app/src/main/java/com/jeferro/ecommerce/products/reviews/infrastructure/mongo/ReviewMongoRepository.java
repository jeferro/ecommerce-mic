package com.jeferro.ecommerce.products.reviews.infrastructure.mongo;

import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.ecommerce.products.reviews.infrastructure.mongo.daos.ReviewMongoDao;
import com.jeferro.ecommerce.products.reviews.infrastructure.mongo.mappers.ReviewMongoMapper;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMongoRepository implements ReviewsRepository {

  private final ReviewMongoMapper reviewMongoMapper;

  private final ReviewMongoDao reviewMongoDao;

  @Override
  public Review save(Review review) {
    var reviewDto = reviewMongoMapper.toDTO(review);

    var resultDto = reviewMongoDao.save(reviewDto);

    return reviewMongoMapper.toDomain(resultDto);
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
    var productReviewIds =
        reviews.stream().map(Entity::getId).map(reviewMongoMapper::toDTO).toList();

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
