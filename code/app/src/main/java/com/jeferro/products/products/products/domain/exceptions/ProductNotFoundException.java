package com.jeferro.products.products.products.domain.exceptions;

import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_NOT_FOUND;

public class ProductNotFoundException extends NotFoundException {

    private ProductNotFoundException(String message) {
        super(PRODUCT_NOT_FOUND, "Product not found", message);
    }

    public static ProductNotFoundException createOf(ProductId id) {
        return new ProductNotFoundException("Product " + id + " not found");
    }
}
