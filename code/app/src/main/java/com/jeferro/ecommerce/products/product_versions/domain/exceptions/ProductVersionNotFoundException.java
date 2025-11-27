package com.jeferro.ecommerce.products.product_versions.domain.exceptions;

import static com.jeferro.ecommerce.shared.domain.exceptions.ProductExceptionCodes.PRODUCT_VERSION_NOT_FOUND;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;

public class ProductVersionNotFoundException extends NotFoundException {

  protected ProductVersionNotFoundException(String code, String title, String message) {
    super(code, title, message);
  }

  public static ProductVersionNotFoundException createOf(ProductVersionId versionId) {
    return new ProductVersionNotFoundException(
        PRODUCT_VERSION_NOT_FOUND, "Product not found", "Product '" + versionId + "' not found");
  }
}
