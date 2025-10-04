package com.jeferro.products.products.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.products.parametrics.domain.services.ParametricValidator;
import com.jeferro.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.domain.exceptions.ProductVersionAlreadyExistsException;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProductUseCase extends UseCase<CreateProductParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final ParametricValidator parametricValidator;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, CreateProductParams params) {
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
    var previousVersionCriteria = ProductVersionCriteria.previousProduct(versionId);
    var previousVersion =
        productVersionRepository.findAll(previousVersionCriteria).getFirstOrNull();

    if (previousVersion == null) {
      return;
    }

    previousVersion.expireBeforeVersion(versionId);

    productVersionRepository.save(previousVersion);

    eventBus.sendAll(previousVersion);
  }

  private ProductVersion createNewVersion(
      ProductVersionId versionId, ParametricValueId typeId, LocalizedField name) {
    var nextVersionCriteria = ProductVersionCriteria.nextProduct(versionId);
    var nextVersion = productVersionRepository.findAll(nextVersionCriteria).getFirstOrNull();

    var newVersion = ProductVersion.create(versionId, typeId, name, nextVersion);

    productVersionRepository.save(newVersion);

    eventBus.sendAll(newVersion);

    return newVersion;
  }
}
