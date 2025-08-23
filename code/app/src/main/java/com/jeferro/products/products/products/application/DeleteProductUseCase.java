package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.DeleteProductParams;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
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
public class DeleteProductUseCase extends UseCase<DeleteProductParams, ProductVersion> {

    private final ProductVersionRepository productVersionRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public ProductVersion execute(Context context, DeleteProductParams params) {
        var version = ensureProductVersionExists(params);

        deleteProductVersion(version);

        setEndEffectiveDateOfPreviousVersion(version.getVersionId());

        return version;
    }

    private void setEndEffectiveDateOfPreviousVersion(ProductVersionId versionId) {
        var previousVersionFilter = ProductFilter.previousProduct(versionId);
        var previousVersion = productVersionRepository.findAll(previousVersionFilter).getFirstOrNull();

        if(previousVersion == null){
            return;
        }

        var nextVersionFilter = ProductFilter.nextProduct(versionId);
        var nextVersion = productVersionRepository.findAll(nextVersionFilter).getFirstOrNull();

        if(nextVersion != null) {
            previousVersion.expireBeforeVersion(nextVersion.getVersionId());
        }
        else{
            previousVersion.notExpire();
        }

        productVersionRepository.save(previousVersion);

        eventBus.sendAll(previousVersion);
    }

    private ProductVersion ensureProductVersionExists(DeleteProductParams params) {
        var versionId = params.getVersionId();

        return productVersionRepository.findByIdOrError(versionId);
    }

    private void deleteProductVersion(ProductVersion version) {
        version.delete();

        productVersionRepository.deleteById(version.getVersionId());

        eventBus.sendAll(version);
    }
}
