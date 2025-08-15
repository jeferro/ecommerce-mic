package com.jeferro.products.products.products.application;

import com.jeferro.products.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.products.products.domain.repositories.ProductsInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        var databaseData = givenSeveralProductsInDatabase();

        var params = new SearchProductsParams(
                ProductFilter.createEmpty()
        );

        var result = searchProductsUseCase.execute(
            ContextMother.user(),
            params);

        assertEquals(2, result.size());
        assertTrue(result.contains(databaseData.apple()));
        assertTrue(result.contains(databaseData.pear()));
    }

    @Test
    void givenTwoProducts_whenListProducts_thenReturnsFilteredProducts() {
        var databaseData = givenSeveralProductsInDatabase();

        var userContext = ContextMother.user();
        var params = new SearchProductsParams(
                ProductFilter.searchName("pe")
        );

        var result = searchProductsUseCase.execute(userContext, params);

        assertEquals(1, result.size());
        assertFalse(result.contains(databaseData.apple()));
        assertTrue(result.contains(databaseData.pear()));
    }

    @Test
    void givenNoProducts_whenListProduct_thenReturnsEmpty() {
        var userContext = ContextMother.user();
        var params = new SearchProductsParams(
                ProductFilter.createEmpty()
        );

        var result = searchProductsUseCase.execute(userContext, params);

        assertTrue(result.isEmpty());
    }

    private DatabaseData givenSeveralProductsInDatabase() {
        var apple = ProductMother.apple();
        var pear = ProductMother.pear();
        productsInMemoryRepository.init(apple, pear);

        return new DatabaseData(apple, pear);
    }

    private record DatabaseData(Product apple, Product pear) {

    }
}
