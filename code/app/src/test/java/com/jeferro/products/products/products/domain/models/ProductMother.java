package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;

public class ProductMother {

    public static Product apple() {
        var productCode = ProductCodeMother.appleCode();
        var startEffectiveDate = Instant.parse("2025-08-15T10:15:30.00Z");
        var productId = ProductId.createOf(productCode, startEffectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Apple",
                "es-ES", "Manzana");

        return new Product(productId, name, fruitId, PUBLISHED);
    }

    public static Product pear() {
        var productCode = ProductCodeMother.pearCode();
        var startEffectiveDate = Instant.parse("2025-08-15T10:15:30.00Z");
        var productId = ProductId.createOf(productCode, startEffectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Pear",
                "es-ES", "Pera");

        return new Product(productId, name, fruitId, PUBLISHED);
    }

}
