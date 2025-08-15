package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.UnpublishProductParams;
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
public class UnpublishProductUseCase extends UseCase<UnpublishProductParams, Product> {

    private final ProductsRepository productsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Product execute(Context context, UnpublishProductParams params) {
        var product = ensureProductExists(params);

        return unpublishProduct(params, product);
    }

    private Product ensureProductExists(UnpublishProductParams params) {
        var id = params.getId();

        return productsRepository.findByIdOrError(id);
    }

    private Product unpublishProduct(UnpublishProductParams params, Product product) {
        product.unpublish();

        productsRepository.save(product);

        eventBus.sendAll(product);

        return product;
    }
}
