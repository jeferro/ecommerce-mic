package com.jeferro.ecommerce.products.product_versions.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_ALREADY_EXISTS;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ProductVersionAlreadyExistsException extends NotFoundException {

  protected ProductVersionAlreadyExistsException(String code, String message) {
    super(code, "Product already exists", message);
  }

  public static ProductVersionAlreadyExistsException createOf(ProductVersionId versionId) {
    return new ProductVersionAlreadyExistsException(
        PRODUCT_VERSION_ALREADY_EXISTS,
            "Product '" + versionId + "' already exists");
  }
}
