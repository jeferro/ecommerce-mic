package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.GetProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class GetProductReviewUseCase extends UseCase<GetProductReviewParams, ProductReview> {

    private final ProductReviewsRepository productReviewsRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductReview execute(Context context, GetProductReviewParams params) {
        var productReviewId = params.getProductReviewId();

        return productReviewsRepository.findByIdOrError(productReviewId);
    }
}
