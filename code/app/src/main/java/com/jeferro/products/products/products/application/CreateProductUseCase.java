package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.products.application.params.CreateProductParams;
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
public class CreateProductUseCase extends UseCase<CreateProductParams, Product> {

    private final ProductsRepository productsRepository;

    private final ParametricValidator parametricValidator;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Product execute(Context context, CreateProductParams params) {
        var id = params.getId();
        var typeId = params.getTypeId();
        var name = params.getName();

        parametricValidator.validateProductType(typeId);

        var product = Product.create(id, typeId, name);

        productsRepository.save(product);

        eventBus.sendAll(product);

        return product;
    }
}
