package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.UpdateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProductVersionUseCase extends UseCase<UpdateProductVersionParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, UpdateProductVersionParams params) {
    var version = ensureProductVersionExists(params);

    return updateProductVersion(params, version);
  }

  private ProductVersion ensureProductVersionExists(UpdateProductVersionParams params) {
    var versionId = params.getVersionId();

    return productVersionRepository.findByIdOrError(versionId);
  }

  private ProductVersion updateProductVersion(UpdateProductVersionParams params, ProductVersion version) {
    var name = params.getName();

    version.update(name);

    productVersionRepository.save(version);

    eventBus.sendAll(version);

    return version;
  }
}
