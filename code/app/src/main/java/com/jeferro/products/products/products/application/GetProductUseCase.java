package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.GetProductParams;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.repositories.ProductsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class GetProductUseCase extends UseCase<GetProductParams, Product> {

    private final ProductsRepository productsRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Product execute(Context context, GetProductParams params) {
        var productId = params.getProductId();

        return productsRepository.findByIdOrError(productId);
    }
}
