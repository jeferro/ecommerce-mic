package com.jeferro.products.products.products.domain.exceptions;

import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

import static com.jeferro.products.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_ALREADY_EXISTS;

public class ProductVersionAlreadyExistsException extends NotFoundException {

    private ProductVersionAlreadyExistsException(String message) {
        super(PRODUCT_VERSION_ALREADY_EXISTS, "Product already exists", message);
    }

    public static ProductVersionAlreadyExistsException createOf(ProductVersionId versionId) {
        return new ProductVersionAlreadyExistsException("Product '" + versionId + "' already exists");
    }
}
