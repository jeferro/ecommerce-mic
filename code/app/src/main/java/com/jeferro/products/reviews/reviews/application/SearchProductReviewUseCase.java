package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.SearchProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class SearchProductReviewUseCase extends UseCase<SearchProductReviewParams, PaginatedList<ProductReview>> {

    private final ProductReviewsRepository productReviewsRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public PaginatedList<ProductReview> execute(Context context, SearchProductReviewParams params) {
        var productCode = params.getProductCode();

        return productReviewsRepository.findAllByProductCode(productCode);
    }
}
