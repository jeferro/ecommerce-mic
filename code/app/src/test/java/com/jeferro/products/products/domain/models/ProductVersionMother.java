package com.jeferro.products.products.domain.models;

import com.jeferro.products.parametrics.domain.models.ProductTypeMother;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.time.Instant;

import static com.jeferro.products.products.domain.models.status.ProductStatus.PUBLISHED;
import static com.jeferro.products.products.domain.models.status.ProductStatus.UNPUBLISHED;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ProductVersionMother {

    private static final Instant APPLE_V2_EFFECTIVE_DATE = Instant.parse("2025-02-01T09:00:00.00Z");

    public static ProductVersionSummary appleV1Summary() {
        var productCode = ProductCodeMother.apple();
        var effectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var name = LocalizedField.createOf(
            "en-US", "Apple v1",
            "es-ES", "Manzana v1");

        return new ProductVersionSummary(productId,
            name,
            UNPUBLISHED);
    }

    public static ProductVersion appleV1() {
        var productCode = ProductCodeMother.apple();
        var effectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Apple v1",
                "es-ES", "Manzana v1");

        return new ProductVersion(productId,
            name,
            fruitId,
            APPLE_V2_EFFECTIVE_DATE.minus(1, SECONDS),
            UNPUBLISHED);
    }

    public static ProductVersion appleV2() {
        var productCode = ProductCodeMother.apple();
        var productId = ProductVersionId.createOf(productCode, APPLE_V2_EFFECTIVE_DATE);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Apple v2",
            "es-ES", "Manzana v2");

        return new ProductVersion(productId, name, fruitId, null, PUBLISHED);
    }

    public static ProductVersion pearV1() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
                "en-US", "Pear v1",
                "es-ES", "Pera v1");

        return new ProductVersion(productId, name, fruitId, null, PUBLISHED);
    }

    public static ProductVersionSummary pearV1Summary() {
        var productCode = ProductCodeMother.pear();
        var effectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var name = LocalizedField.createOf(
            "en-US", "Pear v1",
            "es-ES", "Pera v1");

        return new ProductVersionSummary(productId, name, PUBLISHED);
    }

    public static ProductVersion bananaV1() {
        var productCode = ProductCodeMother.banana();
        var effectiveDate = Instant.parse("2025-01-01T09:00:00.00Z");
        var productId = ProductVersionId.createOf(productCode, effectiveDate);

        var fruitId = ProductTypeMother.fruitId();
        var name = LocalizedField.createOf(
            "en-US", "Banana",
            "es-ES", "Plátano");

        return new ProductVersion(productId, name, fruitId, null, PUBLISHED);
    }

}
