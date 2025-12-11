package com.jeferro.ecommerce.products.product_versions.domain.exceptions;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_ALREADY_EXISTS;

public class ProductVersionAlreadyExistsException extends ValueValidationException {

  protected ProductVersionAlreadyExistsException(String code, String message) {
    super(code, "Product already exists", message);
  }

  public static ProductVersionAlreadyExistsException createOf(ProductVersionId versionId) {
    return new ProductVersionAlreadyExistsException(
        PRODUCT_VERSION_ALREADY_EXISTS,
            "Product '" + versionId + "' already exists");
  }
}
