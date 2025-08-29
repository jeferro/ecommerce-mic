package com.jeferro.products.reviews.reviews.infrastructure.mongo;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewFilter;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.daos.ReviewMongoDao;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos.ReviewMongoDTO;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.mappers.ReviewMongoMapper;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.services.ReviewQueryMongoCreator;
import com.jeferro.shared.auth.infrastructure.mongo.services.CustomMongoTemplate;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewMongoRepository implements ReviewsRepository {

    private final ReviewMongoMapper reviewMongoMapper = ReviewMongoMapper.INSTANCE;

    private final ReviewMongoDao reviewMongoDao;

    private final ReviewQueryMongoCreator reviewQueryMongoCreator;

    private final CustomMongoTemplate customMongoTemplate;

    @Override
    public void save(Review review) {
        var reviewDto = reviewMongoMapper.toDTO(review);

        reviewMongoDao.save(reviewDto);
    }

    @Override
    public PaginatedList<Review> findAll(ReviewFilter filter) {
        Query query = reviewQueryMongoCreator.create(filter);

        Page<ReviewMongoDTO> page = customMongoTemplate.findPage(query, ReviewMongoDTO.class);

        List<Review> entities = page.getContent().stream()
            .map(reviewMongoMapper::toDomain)
            .toList();

        return PaginatedList.createOfFilter(entities, filter, page.getTotalElements());
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
