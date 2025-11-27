package com.jeferro.ecommerce.products.product_versions.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_ALREADY_EXISTS;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ProductVersionAlreadyExistsException extends NotFoundException {

  protected ProductVersionAlreadyExistsException(String code, String title, String message) {
    super(code, title, message);
  }

  public static ProductVersionAlreadyExistsException createOf(ProductVersionId versionId) {
    return new ProductVersionAlreadyExistsException(
        PRODUCT_VERSION_ALREADY_EXISTS,
        "Product already exists",
        "Product '" + versionId + "' already exists");
  }
}
