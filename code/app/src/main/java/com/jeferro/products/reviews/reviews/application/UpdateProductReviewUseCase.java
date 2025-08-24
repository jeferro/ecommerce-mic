package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.UpdateProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.repositories.ProductReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class UpdateProductReviewUseCase extends UseCase<UpdateProductReviewParams, ProductReview> {

    private final ProductReviewsRepository productReviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductReview execute(Context context, UpdateProductReviewParams params) {
        var productReview = ensureProductReviewExists(params);

        return updateProductReview(context, params, productReview);
    }

    private ProductReview ensureProductReviewExists(UpdateProductReviewParams params) {
        var productReviewId = params.getProductReviewId();

        return productReviewsRepository.findByIdOrError(productReviewId);
    }

    private ProductReview updateProductReview(Context context, UpdateProductReviewParams params, ProductReview productReview) {
        var auth = context.getAuth();
        var locale = context.getLocale();

        var comment = params.getComment();

        productReview.update(comment, locale, auth);

        productReviewsRepository.save(productReview);

        eventBus.sendAll(productReview);

        return productReview;
    }
}
