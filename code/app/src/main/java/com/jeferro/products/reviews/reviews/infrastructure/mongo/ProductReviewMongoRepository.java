package com.jeferro.products.reviews.reviews.infrastructure.mongo;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsRepository;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.daos.ProductReviewMongoDao;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.mappers.ProductReviewMongoMapper;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductReviewMongoRepository implements ProductReviewsRepository {

    private final ProductReviewMongoMapper productReviewMongoMapper = ProductReviewMongoMapper.INSTANCE;

    private final ProductReviewMongoDao productReviewMongoDao;

    @Override
    public void save(ProductReview productReview) {
        var productReviewDto = productReviewMongoMapper.toDTO(productReview);

        productReviewMongoDao.save(productReviewDto);
    }

    @Override
    public PaginatedList<ProductReview> findAllByProductCode(ProductCode productCode) {
        var productCodeDto = productReviewMongoMapper.toDTO(productCode);

        var products = productReviewMongoDao.findAllByProductCode(productCodeDto).stream()
                .map(productReviewMongoMapper::toDomain)
                .toList();

        return PaginatedList.createOfList(products);
    }

    @Override
    public Optional<ProductReview> findById(ProductReviewId productReviewId) {
        var productReviewIdDto = productReviewMongoMapper.toDTO(productReviewId);

        return productReviewMongoDao.findById(productReviewIdDto)
                .map(productReviewMongoMapper::toDomain);
    }

    @Override
    public void deleteById(ProductReviewId productReviewId) {
        var productReviewIdDto = productReviewMongoMapper.toDTO(productReviewId);

        productReviewMongoDao.deleteById(productReviewIdDto);
    }

    @Override
    public void deleteAll(PaginatedList<ProductReview> productReviews) {
        var productReviewIds = productReviews.stream()
                .map(Entity::getId)
                .map(productReviewMongoMapper::toDTO)
                .toList();

        productReviewMongoDao.deleteAllById(productReviewIds);
    }
}
