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
    var productVersionId = params.getProductVersionId();
    var typeId = params.getTypeId();
    var name = params.getName();

    ensureProductVersionNotExist(productVersionId);

    parametricValidator.validateProductType(typeId);

    setEndEffectiveDateOfPreviousProduct(productVersionId);

    return createNewVersion(productVersionId, typeId, name);
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

    var previousProductVersion = previousVersionOpt.get();

    previousProductVersion.expireBeforeVersion(versionId);

    productVersionRepository.save(previousProductVersion);

    eventBus.sendAll(previousProductVersion);
  }

  private ProductVersion createNewVersion(ProductVersionId versionId, ParametricValueId typeId, LocalizedField name) {
    var nextVersionCriteria = ProductVersionCriteria.nextProductVersion(versionId);

    var nextProductVersion = productVersionRepository.findOne(nextVersionCriteria).orElse(null);

    var newVersion = ProductVersion.create(versionId, typeId, name, nextProductVersion);

    productVersionRepository.save(newVersion);

    eventBus.sendAll(newVersion);

    return newVersion;
  }
}
