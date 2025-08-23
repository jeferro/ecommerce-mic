package com.jeferro.products.products.product_reviews.domain.repositories;

import com.jeferro.products.products.product_reviews.domain.models.ProductReview;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

public class ProductReviewsInMemoryRepository extends InMemoryRepository<ProductReview, ProductReviewId>
        implements ProductReviewsRepository {

    public ProductReviewsInMemoryRepository() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        data.put(johnReviewOfApple.getId(), johnReviewOfApple);

        var emilyReviewOfApple = ProductReviewMother.emilyReviewOfApple();
        data.put(emilyReviewOfApple.getId(), emilyReviewOfApple);
    }

    @Override
    public PaginatedList<ProductReview> findAllByProductCode(ProductCode productCode) {
        var products = data.values().stream()
                .filter(productReview -> productReview.getProductCode().equals(productCode))
                .toList();

        return PaginatedList.createOfList(products);
    }

    @Override
    public void deleteAll(PaginatedList<ProductReview> productReviews) {
        productReviews.stream()
                .map(Entity::getId)
                .forEach(this::deleteById);
    }
}
