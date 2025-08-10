package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.PublishProductParams;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.repositories.ProductsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class PublishProductUseCase extends UseCase<PublishProductParams, Product> {

    private final ProductsRepository productsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Product execute(Context context, PublishProductParams params) {
        var product = ensureProductExists(params);

        return publishProduct(product);
    }

    private Product ensureProductExists(PublishProductParams params) {
        var productId = params.getProductId();

        return productsRepository.findByIdOrError(productId);
    }

    private Product publishProduct(Product product) {
        product.publish();

        productsRepository.save(product);

        eventBus.sendAll(product);

        return product;
    }
}
