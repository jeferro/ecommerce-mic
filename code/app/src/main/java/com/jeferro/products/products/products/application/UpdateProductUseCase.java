package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.UpdateProductParams;
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
public class UpdateProductUseCase extends UseCase<UpdateProductParams, Product> {

    private final ProductsRepository productsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Product execute(Context context, UpdateProductParams params) {
        var product = ensureProductExists(params);

        return updateProduct(params, product);
    }

    private Product ensureProductExists(UpdateProductParams params) {
        var productId = params.getProductId();

        return productsRepository.findByIdOrError(productId);
    }

    private Product updateProduct(UpdateProductParams params, Product product) {
        var name = params.getName();

        product.update(name);

        productsRepository.save(product);

        eventBus.sendAll(product);

        return product;
    }
}
