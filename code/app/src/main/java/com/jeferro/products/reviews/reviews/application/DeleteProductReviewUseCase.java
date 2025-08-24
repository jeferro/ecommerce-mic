package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.DeleteProductReviewParams;
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
public class DeleteProductReviewUseCase extends UseCase<DeleteProductReviewParams, ProductReview> {

    private final ProductReviewsRepository productReviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductReview execute(Context context, DeleteProductReviewParams params) {
        var productReview = ensureProductReviewExists(params);

        return deleteProductReview(context, productReview);
    }

    private ProductReview ensureProductReviewExists(DeleteProductReviewParams params) {
        var productReviewId = params.getProductReviewId();

        return productReviewsRepository.findByIdOrError(productReviewId);
    }

    private ProductReview deleteProductReview(Context context, ProductReview productReview) {
        var auth = context.getAuth();

        productReview.deleteByUser(auth);

        productReviewsRepository.deleteById(productReview.getId());

        eventBus.sendAll(productReview);

        return productReview;
    }
}
