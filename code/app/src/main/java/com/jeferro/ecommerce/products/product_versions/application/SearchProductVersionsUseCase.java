package com.jeferro.ecommerce.products.product_versions.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.products.product_versions.application.params.SearchProductVersionsParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.utils.FutureUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProductVersionsUseCase
    extends UseCase<SearchProductVersionsParams, PaginatedList<ProductVersionSummary>> {

  private final ProductVersionRepository productVersionRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public PaginatedList<ProductVersionSummary> execute(Auth auth, SearchProductVersionsParams params) {
    var criteria = params.getCriteria();

    return FutureUtils.async(
        () -> productVersionRepository.findAllSummary(criteria),
        () -> productVersionRepository.count(criteria),
        (summaries, totalReviews) ->
            PaginatedList.createOfCriteria(criteria, summaries, totalReviews));
  }
}
