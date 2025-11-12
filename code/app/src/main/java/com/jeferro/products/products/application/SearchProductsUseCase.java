package com.jeferro.products.products.application;

import static com.jeferro.products.shared.application.Roles.USER;

import com.jeferro.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.shared.utils.FutureUtils;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProductsUseCase
    extends UseCase<SearchProductsParams, PaginatedList<ProductVersionSummary>> {

  private final ProductVersionRepository productVersionRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public PaginatedList<ProductVersionSummary> execute(Auth auth, SearchProductsParams params) {
    var criteria = params.getCriteria();

    return FutureUtils.async(
        () -> productVersionRepository.findAllSummary(criteria),
        () -> productVersionRepository.count(criteria),
        (summaries, totalReviews) ->
            PaginatedList.createOfCriteria(criteria, summaries, totalReviews));
  }
}
