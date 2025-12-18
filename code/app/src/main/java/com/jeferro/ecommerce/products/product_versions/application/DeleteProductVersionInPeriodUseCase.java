package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionOrder.START_EFFECTIVE_DATE;
import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.DeleteProductVersionInPeriodParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductCode;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProductVersionInPeriodUseCase
    extends UseCase<DeleteProductVersionInPeriodParams, Void> {

  private final ProductVersionRepository productVersionRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public Void execute(Auth auth, DeleteProductVersionInPeriodParams params) {
    var productCode = params.getProductCode();
    var startDate = params.getStartDate();
    var endDate = params.getEndDate();

    var overlappingVersions = findOverlappingVersions(productCode, startDate, endDate);

    deleteOverlappingVersions(overlappingVersions);

    updatePreviousVersion(productCode, startDate, endDate);

    return null;
  }

  private List<ProductVersion> findOverlappingVersions(
      ProductCode productCode, Instant startDate, Instant endDate) {
    var criteria = ProductVersionCriteria.overlappingDateRange(productCode, startDate, endDate);

    return productVersionRepository.findAll(criteria);
  }

  private void deleteOverlappingVersions(List<ProductVersion> versions) {
    for (var version : versions) {
      version.delete();
      productVersionRepository.delete(version);
      eventBus.sendAll(version);
    }
  }

  private void updatePreviousVersion(ProductCode productCode, Instant startDate, Instant endDate) {
    var previousVersionCriteria =
        new ProductVersionCriteria(
            0, 1, START_EFFECTIVE_DATE, false, productCode, null, startDate, null, null, null);

    var previousVersionOpt = productVersionRepository.findOne(previousVersionCriteria);

    if (previousVersionOpt.isEmpty()) {
      return;
    }

    var previousVersion = previousVersionOpt.get();

    var nextVersionCriteria =
        new ProductVersionCriteria(
            0, 1, START_EFFECTIVE_DATE, true, productCode, endDate, null, null, null, null);

    var nextVersionOpt = productVersionRepository.findOne(nextVersionCriteria);

    if (nextVersionOpt.isPresent()) {
      var nextVersion = nextVersionOpt.get();
      previousVersion.expireBeforeVersion(nextVersion.getId());
    } else {
      previousVersion.notExpire();
    }

    productVersionRepository.save(previousVersion);
    eventBus.sendAll(previousVersion);
  }
}

