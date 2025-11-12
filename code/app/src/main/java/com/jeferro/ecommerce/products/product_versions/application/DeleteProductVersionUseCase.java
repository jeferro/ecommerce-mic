package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.DeleteProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProductVersionUseCase extends UseCase<DeleteProductVersionParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, DeleteProductVersionParams params) {
    var version = ensureProductVersionExists(params);

    deleteProductVersion(version);

    setEndEffectiveDateOfPreviousVersion(version.getVersionId());

    return version;
  }

  private void setEndEffectiveDateOfPreviousVersion(ProductVersionId versionId) {
    var previousVersionCriteria = ProductVersionCriteria.previousProduct(versionId);
    var previousVersionOpt = productVersionRepository.findOne(previousVersionCriteria);

    if (previousVersionOpt.isEmpty()) {
      return;
    }

    var previousVersion = previousVersionOpt.get();

    var nextVersionCriteria = ProductVersionCriteria.nextProduct(versionId);
    var nextVersionOpt = productVersionRepository.findOne(nextVersionCriteria);

    if (nextVersionOpt.isPresent()) {
      var nextVersion = nextVersionOpt.get();
      previousVersion.expireBeforeVersion(nextVersion.getVersionId());
    } else {
      previousVersion.notExpire();
    }

    productVersionRepository.save(previousVersion);

    eventBus.sendAll(previousVersion);
  }

  private ProductVersion ensureProductVersionExists(DeleteProductVersionParams params) {
    var versionId = params.getVersionId();

    return productVersionRepository.findByIdOrError(versionId);
  }

  private void deleteProductVersion(ProductVersion version) {
    version.delete();

    productVersionRepository.delete(version);

    eventBus.sendAll(version);
  }
}
