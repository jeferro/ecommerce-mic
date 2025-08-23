package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.GetProductParams;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class GetProductUseCase extends UseCase<GetProductParams, ProductVersion> {

    private final ProductVersionRepository productVersionRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductVersion execute(Context context, GetProductParams params) {
        var versionId = params.getVersionId();

        return productVersionRepository.findByIdOrError(versionId);
    }
}
