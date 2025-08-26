package com.jeferro.products.reviews.reviews.infrastructure.mongo;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.daos.ReviewMongoDao;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.mappers.ReviewMongoMapper;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewMongoRepository implements ReviewsRepository {

    private final ReviewMongoMapper reviewMongoMapper = ReviewMongoMapper.INSTANCE;

    private final ReviewMongoDao reviewMongoDao;

    @Override
    public void save(Review review) {
        var productReviewDto = reviewMongoMapper.toDTO(review);

        reviewMongoDao.save(productReviewDto);
    }

    @Override
    public PaginatedList<Review> findAllByProductCode(ProductCode productCode) {
        var productCodeDto = reviewMongoMapper.toDTO(productCode);

        var products = reviewMongoDao.findAllByProductCode(productCodeDto).stream()
                .map(reviewMongoMapper::toDomain)
                .toList();

        return PaginatedList.createOfList(products);
    }

    @Override
    public Optional<Review> findById(ReviewId reviewId) {
        var productReviewIdDto = reviewMongoMapper.toDTO(reviewId);

        return reviewMongoDao.findById(productReviewIdDto)
                .map(reviewMongoMapper::toDomain);
    }

    @Override
    public void deleteById(ReviewId reviewId) {
        var productReviewIdDto = reviewMongoMapper.toDTO(reviewId);

        reviewMongoDao.deleteById(productReviewIdDto);
    }

    @Override
    public void deleteAll(PaginatedList<Review> reviews) {
        var productReviewIds = reviews.stream()
                .map(Entity::getId)
                .map(reviewMongoMapper::toDTO)
                .toList();

        reviewMongoDao.deleteAllById(productReviewIds);
    }
}
