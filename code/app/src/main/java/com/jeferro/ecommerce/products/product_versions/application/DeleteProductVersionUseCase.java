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
    var productVersion = findProductVersionOfError(params);

    deleteProductVersion(productVersion);

    setEndEffectiveDateOfPreviousVersion(productVersion.getId());

    return productVersion;
  }

  private ProductVersion findProductVersionOfError(DeleteProductVersionParams params) {
    var productVersionId = params.getProductVersionId();

    return productVersionRepository.findByIdOrError(productVersionId);
  }

  private void deleteProductVersion(ProductVersion productVersion) {
    productVersion.delete();

    productVersionRepository.delete(productVersion);

    eventBus.sendAll(productVersion);
  }

  private void setEndEffectiveDateOfPreviousVersion(ProductVersionId versionId) {
    var previousProductVersionCriteria = ProductVersionCriteria.previousProductVersion(versionId);
    var previousProductVersionOpt = productVersionRepository.findOne(previousProductVersionCriteria);

    if (previousProductVersionOpt.isEmpty()) {
      return;
    }

    var previousProductVersion = previousProductVersionOpt.get();

    var nextVersionCriteria = ProductVersionCriteria.nextProductVersion(versionId);
    var nextVersionOpt = productVersionRepository.findOne(nextVersionCriteria);

    if (nextVersionOpt.isPresent()) {
      var nextVersion = nextVersionOpt.get();
      previousProductVersion.expireBeforeVersion(nextVersion.getId());
    } else {
      previousProductVersion.notExpire();
    }

    productVersionRepository.save(previousProductVersion);

    eventBus.sendAll(previousProductVersion);
  }
}
