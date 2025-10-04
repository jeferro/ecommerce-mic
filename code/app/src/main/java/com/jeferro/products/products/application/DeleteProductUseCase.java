package com.jeferro.products.products.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.products.application.params.DeleteProductParams;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
  public ProductVersion execute(Auth auth, DeleteProductParams params) {
    var version = ensureProductVersionExists(params);

    deleteProductVersion(version);

    setEndEffectiveDateOfPreviousVersion(version.getVersionId());

    return version;
  }

  private void setEndEffectiveDateOfPreviousVersion(ProductVersionId versionId) {
    var previousVersionCriteria = ProductVersionCriteria.previousProduct(versionId);
    var previousVersion =
        productVersionRepository.findAll(previousVersionCriteria).getFirstOrNull();

    if (previousVersion == null) {
      return;
    }

    var nextVersionCriteria = ProductVersionCriteria.nextProduct(versionId);
    var nextVersion = productVersionRepository.findAll(nextVersionCriteria).getFirstOrNull();

    if (nextVersion != null) {
      previousVersion.expireBeforeVersion(nextVersion.getVersionId());
    } else {
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

    productVersionRepository.delete(version);

    eventBus.sendAll(version);
  }
}
