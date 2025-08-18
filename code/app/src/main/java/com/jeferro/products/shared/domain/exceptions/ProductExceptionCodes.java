package com.jeferro.products.shared.domain.exceptions;

public interface ProductExceptionCodes {
    // Parametrics
    String PARAMETRIC_NOT_FOUND = "parametric-not-found";
    String PARAMETRIC_VALUE_NOT_FOUND = "parametric-value-not-found";

    // Products
    String PRODUCT_VERSION_NOT_FOUND = "product-version-not-found";
    String PRODUCT_VERSION_ALREADY_EXISTS = "product-version-already-exists";

    // Product Reviews
    String REVIEW_NOT_FOUND = "product-review-not-found";
    String REVIEW_ALREADY_EXISTS = "product-review-already-exists";
}
