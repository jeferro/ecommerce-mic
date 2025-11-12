package com.jeferro.ecommerce.products.product_versions.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeferro.ecommerce.products.product_versions.application.params.SearchProductVersionsParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchProductVersionsUseCaseTest {

  private ProductVersionInMemoryRepository productsInMemoryRepository;

  private SearchProductVersionsUseCase searchProductVersionsUseCase;

  @BeforeEach
  void beforeEach() {
    productsInMemoryRepository = new ProductVersionInMemoryRepository();

    searchProductVersionsUseCase = new SearchProductVersionsUseCase(productsInMemoryRepository);
  }

  @Test
  void should_returnProducts_when_exist() {
    var params = new SearchProductVersionsParams(ProductVersionCriteria.allPage());

    var result = searchProductVersionsUseCase.execute(AuthMother.john(), params);

    assertEquals(3, result.size());

    var appleV1Summary = ProductVersionMother.appleV1Summary();
    assertTrue(result.contains(appleV1Summary));

    var pearV1Summary = ProductVersionMother.pearV1Summary();
    assertTrue(result.contains(pearV1Summary));
  }

  @Test
  void should_returnFilteredProduct_when_exist() {
    var appleV1 = ProductVersionMother.appleV1();

    var params = new SearchProductVersionsParams(ProductVersionCriteria.byCodePage(appleV1.getCode()));

    var result = searchProductVersionsUseCase.execute(AuthMother.john(), params);

    result.forEach(productVersion -> assertEquals(appleV1.getCode(), productVersion.getCode()));
  }

  @Test
  void should_returnEmptyList_when_productsNotExist() {
    productsInMemoryRepository.clear();

    var params = new SearchProductVersionsParams(ProductVersionCriteria.allPage());

    var result = searchProductVersionsUseCase.execute(AuthMother.john(), params);

    assertTrue(result.isEmpty());
  }
}
