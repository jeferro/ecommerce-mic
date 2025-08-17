package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchProductsUseCaseTest {

    private ProductsInMemoryRepository productsInMemoryRepository;

    private SearchProductsUseCase searchProductsUseCase;

    @BeforeEach
    void beforeEach() {
        productsInMemoryRepository = new ProductsInMemoryRepository();

        searchProductsUseCase = new SearchProductsUseCase(productsInMemoryRepository);
    }

    @Test
    void givenTwoProducts_whenListProducts_thenReturnsAllProducts() {
        var params = new SearchProductsParams(
                ProductFilter.createEmpty()
        );

        var result = searchProductsUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(2, result.size());

        var appleV1 = ProductMother.appleV1();
        assertTrue(result.contains(appleV1));

        var pearV1 = ProductMother.pearV1();
        assertTrue(result.contains(pearV1));
    }

    @Test
    void givenTwoProducts_whenListProducts_thenReturnsFilteredProducts() {
        var params = new SearchProductsParams(
                ProductFilter.searchName("pe")
        );

        var result = searchProductsUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(1, result.size());

        var appleV1 = ProductMother.appleV1();
        assertFalse(result.contains(appleV1));

        var pearV1 = ProductMother.pearV1();
        assertTrue(result.contains(pearV1));
    }

    @Test
    void givenNoProducts_whenListProduct_thenReturnsEmpty() {
      productsInMemoryRepository.clear();

        var params = new SearchProductsParams(
                ProductFilter.createEmpty()
        );

        var result = searchProductsUseCase.execute(
            ContextMother.john(),
            params);

        assertTrue(result.isEmpty());
    }
}
