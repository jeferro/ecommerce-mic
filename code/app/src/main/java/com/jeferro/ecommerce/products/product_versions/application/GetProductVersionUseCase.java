package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.GetProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProductVersionUseCase extends UseCase<GetProductVersionParams, ProductVersion> {

  private final ProductVersionRepository productVersionRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public ProductVersion execute(Auth auth, GetProductVersionParams params) {
    var versionId = params.getVersionId();

    return productVersionRepository.findByIdOrError(versionId);
  }
}
