package com.jeferro.products.products.application;

import com.jeferro.products.products.application.params.UpdateProductParams;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class UpdateProductUseCase extends UseCase<UpdateProductParams, ProductVersion> {

    private final ProductVersionRepository productVersionRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductVersion execute(Auth auth, UpdateProductParams params) {
        var version = ensureProductVersionExists(params);

        return updateProductVersion(params, version);
    }

    private ProductVersion ensureProductVersionExists(UpdateProductParams params) {
        var versionId = params.getVersionId();

        return productVersionRepository.findByIdOrError(versionId);
    }

    private ProductVersion updateProductVersion(UpdateProductParams params, ProductVersion version) {
        var name = params.getName();

        version.update(name);

        productVersionRepository.save(version);

        eventBus.sendAll(version);

        return version;
    }
}
