package com.jeferro.products.products.infrastructure.rest_api;

import com.jeferro.products.products.infrastructure.rest_api.dtos.CreateProductVersionInputRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductOrderRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionListRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionSummaryListRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.UpdateProductVersionInputRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.mappers.ProductVersionRestMapper;
import com.jeferro.shared.ddd.application.UseCaseBus;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductVersionRestController implements ProductVersionsApi {

  private final ProductVersionRestMapper productVersionRestMapper =
      ProductVersionRestMapper.INSTANCE;

  private final UseCaseBus useCaseBus;

  @Override
  public ProductVersionSummaryListRestDTO searchProductVersions(
      Integer pageNumber,
      Integer pageSize,
      ProductOrderRestDTO order,
      Boolean ascending,
      OffsetDateTime searchDate) {
    var params =
        productVersionRestMapper.toSearchProductsParams(
            pageNumber, pageSize, order, ascending, null, searchDate);

    var productVersionSummaries = useCaseBus.execute(params);

    return productVersionRestMapper.toSummaryDTO(productVersionSummaries);
  }

  @Override
  public ProductVersionListRestDTO searchProductVersionIds(
      String productCode, Integer pageNumber, Integer pageSize) {
    var params =
        productVersionRestMapper.toSearchProductsParams(
            pageNumber, pageSize, ProductOrderRestDTO.ID, true, productCode, null);

    var productVersionSummaries = useCaseBus.execute(params);

    return productVersionRestMapper.toVersionListDTO(productVersionSummaries);
  }

  @Override
  public ProductVersionRestDTO createProductVersion(
      String productCode,
      String effectiveDate,
      CreateProductVersionInputRestDTO createProductVersionInputRestDTO) {
    var params =
        productVersionRestMapper.toCreateProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate),
            createProductVersionInputRestDTO);

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }

  @Override
  public ProductVersionRestDTO getProductVersion(String productCode, String effectiveDate) {
    var params =
        productVersionRestMapper.toGetProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate));

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }

  @Override
  public ProductVersionRestDTO updateProductVersion(
      String productCode,
      String effectiveDate,
      UpdateProductVersionInputRestDTO updateProductVersionInputRestDTO) {
    var params =
        productVersionRestMapper.toUpdateProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate),
            updateProductVersionInputRestDTO);

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }

  @Override
  public ProductVersionRestDTO publishProductVersion(String productCode, String effectiveDate) {
    var params =
        productVersionRestMapper.toPublishProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate));

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }

  @Override
  public ProductVersionRestDTO unpublishProductVersion(String productCode, String effectiveDate) {
    var params =
        productVersionRestMapper.toUnpublishProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate));

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }

  @Override
  public ProductVersionRestDTO deleteProductVersion(String productCode, String effectiveDate) {
    var params =
        productVersionRestMapper.toDeleteProductParams(
            productVersionRestMapper.toDomain(productCode, effectiveDate));

    var productVersion = useCaseBus.execute(params);

    return productVersionRestMapper.toDTO(productVersion);
  }
}
