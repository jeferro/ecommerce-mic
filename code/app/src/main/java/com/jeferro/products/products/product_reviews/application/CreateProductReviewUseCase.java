package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.CreateProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.exceptions.ProductReviewAlreadyExistsException;
import com.jeferro.products.products.product_reviews.domain.models.ProductReview;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsRepository;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.repositories.ProductsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.exceptions.auth.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.models.auth.UserAuth;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class CreateProductReviewUseCase extends UseCase<CreateProductReviewParams, ProductReview> {

    private final ProductsRepository productsRepository;

    private final ProductReviewsRepository productReviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductReview execute(Context context, CreateProductReviewParams params) {
        Auth auth = context.getAuth();

        var productCode = ensureProductExists(params);

        ensureUserIsAuthenticated(auth);

        ensureProductReviewDoesNotExists(context, productCode);

        return createProductReview(context, params, productCode);
    }

    private void ensureUserIsAuthenticated(Auth auth) {
        if (!(auth instanceof UserAuth)) {
            throw ForbiddenException.createOfNotUserAuth(auth);
        }
    }

    private ProductCode ensureProductExists(CreateProductReviewParams params) {
        var productCode = params.getProductCode();

        var product = productsRepository.findByIdOrError(productCode);

        return product.getCode();
    }

    private void ensureProductReviewDoesNotExists(Context context, ProductCode productCode) {
        var auth = context.getAuth();

        var productReviewId = ProductReviewId.createOf(auth, productCode);

        productReviewsRepository.findById(productReviewId)
                .ifPresent(current -> {
                    throw ProductReviewAlreadyExistsException.createOf(productReviewId);
                });
    }

    private ProductReview createProductReview(Context context, CreateProductReviewParams params, ProductCode productCode) {
        var auth = context.getAuth();
        var locale = context.getLocale();

        var comment = params.getComment();

        var productReview = ProductReview.createOf(productCode, locale, comment, auth);

        productReviewsRepository.save(productReview);

        eventBus.sendAll(productReview);
        return productReview;
    }
}
