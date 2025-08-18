package com.jeferro.products.products.products.application;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.products.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.products.products.domain.repositories.ProductsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import com.jeferro.shared.locale.domain.models.LocalizedField;
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

        ensureVersionNotExists(id);

        parametricValidator.validateProductType(typeId);

        updatePreviousVersionIfExists(id);

        return createNewVersion(id, typeId, name);
    }

    private void ensureVersionNotExists(ProductId id) {
        var version = productsRepository.findById(id);

        if(version.isPresent()){
            throw ProductVersionAlreadyExistsException.createOf(id);
        }
    }

    private void updatePreviousVersionIfExists(ProductId id) {
        var previousVersionFilter = ProductFilter.previousProduct(id);
        var nextVersion = productsRepository.findAll(previousVersionFilter).getFirstOrNull();

        if(nextVersion == null){
            return;
        }

        nextVersion.expireBefore(id);

        productsRepository.save(nextVersion);

        eventBus.sendAll(nextVersion);
    }

    private Product createNewVersion(ProductId id, ParametricValueId typeId, LocalizedField name) {
        var nextVersionFilter = ProductFilter.nextProduct(id);
        var nextVersion = productsRepository.findAll(nextVersionFilter).getFirstOrNull();

        var product = Product.create(id, typeId, name, nextVersion);

        productsRepository.save(product);

        eventBus.sendAll(product);

        return product;
    }
}
