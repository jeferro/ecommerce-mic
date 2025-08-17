package com.jeferro.products.products.products.domain.models;

import com.jeferro.products.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.time.Instant;

import static com.jeferro.products.products.products.domain.models.status.ProductStatus.PUBLISHED;

public class ProductMother {

    public static Product appleV1() {
        var productCode = ProductCodeMother.apple();
        var startEffectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductId.createOf(productCode, startEffectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Apple",
                "es-ES", "Manzana");

        return new Product(productId, name, fruitId, null, PUBLISHED);
    }

    public static Product pearV1() {
        var productCode = ProductCodeMother.pear();
        var startEffectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductId.createOf(productCode, startEffectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Pear",
                "es-ES", "Pera");

        return new Product(productId, name, fruitId, null, PUBLISHED);
    }

    public static Product bananaV1() {
        var productCode = ProductCodeMother.banana();
        var startEffectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductId.createOf(productCode, startEffectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Banana",
            "es-ES", "Plátano");

        return new Product(productId, name, fruitId, null, PUBLISHED);
    }

}
