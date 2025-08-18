package com.jeferro.products.products.products.domain.exceptions;

import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_NOT_FOUND;

public class ProductVersionNotFoundException extends NotFoundException {

    private ProductVersionNotFoundException(String message) {
        super(PRODUCT_VERSION_NOT_FOUND, "Product not found", message);
    }

    public static ProductVersionNotFoundException createOf(ProductId id) {
        return new ProductVersionNotFoundException("Product " + id + " not found");
    }
}
