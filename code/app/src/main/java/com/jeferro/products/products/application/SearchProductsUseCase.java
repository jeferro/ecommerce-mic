package com.jeferro.products.products.application;

import com.jeferro.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class SearchProductsUseCase extends UseCase<SearchProductsParams, PaginatedList<ProductVersion>> {

    private final ProductVersionRepository productVersionRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public PaginatedList<ProductVersion> execute(Auth auth, SearchProductsParams params) {
        var criteria = params.getCriteria();

        return productVersionRepository.findAll(criteria);
    }
}
