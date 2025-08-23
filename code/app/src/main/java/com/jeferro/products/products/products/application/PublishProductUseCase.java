package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.PublishProductParams;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class PublishProductUseCase extends UseCase<PublishProductParams, ProductVersion> {

    private final ProductVersionRepository productVersionRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductVersion execute(Context context, PublishProductParams params) {
        var version = ensureProductVersionExists(params);

        return publishProductVersion(version);
    }

    private ProductVersion ensureProductVersionExists(PublishProductParams params) {
        var versionId = params.getVersionId();

        return productVersionRepository.findByIdOrError(versionId);
    }

    private ProductVersion publishProductVersion(ProductVersion version) {
        version.publish();

        productVersionRepository.save(version);

        eventBus.sendAll(version);

        return version;
    }
}
