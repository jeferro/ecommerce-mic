package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;

public class ProductMother {

    public static Product apple() {
        var productCode = ProductCodeMother.appleCode();
        var effectiveDate = Instant.now();
        var productId = new ProductId(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Apple",
                "es-ES", "Manzana");

        return new Product(productId, name, fruitId, PUBLISHED);
    }

    public static Product pear() {
        var productCode = ProductCodeMother.pearCode();
        var effectiveDate = Instant.now();
        var productId = new ProductId(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Pear",
                "es-ES", "Pera");

        return new Product(productId, name, fruitId, PUBLISHED);
    }

}
