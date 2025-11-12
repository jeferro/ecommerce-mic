package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.ecommerce.support.parametrics.domain.services.ParametricValidator;
import com.jeferro.ecommerce.products.product_versions.application.params.CreateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProductVersionUseCase extends UseCase<CreateProductVersionParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final ParametricValidator parametricValidator;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, CreateProductVersionParams params) {
    var versionId = params.getVersionId();
    var typeId = params.getTypeId();
    var name = params.getName();

    ensureProductVersionNotExist(versionId);

    parametricValidator.validateProductType(typeId);

    setEndEffectiveDateOfPreviousProduct(versionId);

    return createNewVersion(versionId, typeId, name);
  }

  private void ensureProductVersionNotExist(ProductVersionId versionId) {
    var version = productVersionRepository.findById(versionId);

    if (version.isPresent()) {
      throw ProductVersionAlreadyExistsException.createOf(versionId);
    }
  }

  private void setEndEffectiveDateOfPreviousProduct(ProductVersionId versionId) {
    var previousVersionCriteria = ProductVersionCriteria.previousProductVersion(versionId);

    var previousVersionOpt = productVersionRepository.findOne(previousVersionCriteria);

    if (previousVersionOpt.isEmpty()) {
      return;
    }

    var previousVersion = previousVersionOpt.get();

    previousVersion.expireBeforeVersion(versionId);

    productVersionRepository.save(previousVersion);

    eventBus.sendAll(previousVersion);
  }

  private ProductVersion createNewVersion(ProductVersionId versionId, ParametricValueId typeId, LocalizedField name) {
    var nextVersionCriteria = ProductVersionCriteria.nextProductVersion(versionId);

    var nextVersion = productVersionRepository.findOne(nextVersionCriteria).orElse(null);

    var newVersion = ProductVersion.create(versionId, typeId, name, nextVersion);

    productVersionRepository.save(newVersion);

    eventBus.sendAll(newVersion);

    return newVersion;
  }
}
