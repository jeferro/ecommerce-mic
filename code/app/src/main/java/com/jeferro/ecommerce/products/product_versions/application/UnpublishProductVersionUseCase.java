package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.UnpublishProductVersionParams;
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
public class UnpublishProductVersionUseCase extends UseCase<UnpublishProductVersionParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, UnpublishProductVersionParams params) {
    var productVersion = findProductVersionOfError(params);

    return unpublishProductVersion(productVersion, params);
  }

  private ProductVersion findProductVersionOfError(UnpublishProductVersionParams params) {
    var productVersionId = params.getProductVersionId();

    return productVersionRepository.findByIdOrError(productVersionId);
  }

  private ProductVersion unpublishProductVersion(ProductVersion productVersion, UnpublishProductVersionParams params) {
    var version = params.getVersion();

    productVersion.unpublish(version);

    productVersionRepository.save(productVersion);

    eventBus.sendAll(productVersion);

    return productVersion;
  }
}
