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
    var productVersion = findProductVersionOfError(params);

    return updateProductVersion(params, productVersion);
  }

  private ProductVersion findProductVersionOfError(UpdateProductVersionParams params) {
    var productVersionId = params.getProductVersionId();

    return productVersionRepository.findByIdOrError(productVersionId);
  }

  private ProductVersion updateProductVersion(UpdateProductVersionParams params, ProductVersion productVersion) {
    var name = params.getName();
    var version = params.getVersion();

    productVersion.update(name, version);

    eventBus.sendAll(productVersion);

    return productVersionRepository.save(productVersion);
  }
}
