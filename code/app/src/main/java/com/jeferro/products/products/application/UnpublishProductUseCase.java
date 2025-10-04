package com.jeferro.products.products.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.products.application.params.UnpublishProductParams;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnpublishProductUseCase extends UseCase<UnpublishProductParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, UnpublishProductParams params) {
    var version = ensureProductVersionExists(params);

    return unpublishProductVersion(version);
  }

  private ProductVersion ensureProductVersionExists(UnpublishProductParams params) {
    var versionId = params.getVersionId();

    return productVersionRepository.findByIdOrError(versionId);
  }

  private ProductVersion unpublishProductVersion(ProductVersion version) {
    version.unpublish();

    productVersionRepository.save(version);

    eventBus.sendAll(version);

    return version;
  }
}
