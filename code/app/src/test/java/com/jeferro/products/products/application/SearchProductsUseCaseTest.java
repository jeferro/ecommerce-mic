package com.jeferro.products.products.application;

import com.jeferro.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.products.domain.models.filter.ProductVersionCriteria;
import com.jeferro.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchProductsUseCaseTest {

    private ProductVersionInMemoryRepository productsInMemoryRepository;

    private SearchProductsUseCase searchProductsUseCase;

    @BeforeEach
    void beforeEach() {
        productsInMemoryRepository = new ProductVersionInMemoryRepository();

        searchProductsUseCase = new SearchProductsUseCase(productsInMemoryRepository);
    }

    @Test
    void should_returnProducts_when_exist() {
        var params = new SearchProductsParams(
                ProductVersionCriteria.createEmpty()
        );

        var result = searchProductsUseCase.execute(
            AuthMother.john(),
            params);

        assertEquals(3, result.size());

        var appleV1 = ProductVersionMother.appleV1();
        assertTrue(result.contains(appleV1));

        var pearV1 = ProductVersionMother.pearV1();
        assertTrue(result.contains(pearV1));
    }

    @Test
    void should_returnFilteredProduct_when_exist() {
        var appleV1 = ProductVersionMother.appleV1();

        var params = new SearchProductsParams(
                ProductVersionCriteria.byCode(appleV1.getCode())
        );

        var result = searchProductsUseCase.execute(
            AuthMother.john(),
            params);

        result.forEach(productVersion -> assertEquals(appleV1.getCode(), productVersion.getCode()));
    }

    @Test
    void should_returnEmptyList_when_productsNotExist() {
      productsInMemoryRepository.clear();

        var params = new SearchProductsParams(
                ProductVersionCriteria.createEmpty()
        );

        var result = searchProductsUseCase.execute(
            AuthMother.john(),
            params);

        assertTrue(result.isEmpty());
    }
}
