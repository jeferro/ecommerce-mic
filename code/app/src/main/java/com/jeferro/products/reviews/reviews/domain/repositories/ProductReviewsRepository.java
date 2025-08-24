package com.jeferro.products.reviews.reviews.domain.repositories;

import com.jeferro.products.reviews.reviews.domain.exceptions.ProductReviewNotFoundException;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.Optional;

public interface ProductReviewsRepository {

    void save(ProductReview productReview);

    PaginatedList<ProductReview> findAllByProductCode(ProductCode productCode);

    Optional<ProductReview> findById(ProductReviewId productReviewId);

    default ProductReview findByIdOrError(ProductReviewId productReviewId) {
        return findById(productReviewId)
                .orElseThrow(() -> ProductReviewNotFoundException.createOf(productReviewId));
    }

    void deleteById(ProductReviewId productReviewId);

    void deleteAll(PaginatedList<ProductReview> productReviews);
}
