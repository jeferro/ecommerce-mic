package com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.mappers;

import com.jeferro.ecommerce.products.product_versions.application.params.CreateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.application.params.DeleteProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.application.params.GetProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.application.params.PublishProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.application.params.SearchProductVersionsParams;
import com.jeferro.ecommerce.products.product_versions.application.params.UnpublishProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.application.params.UpdateProductVersionParams;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.CreateProductVersionInputRestDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.ProductOrderRestDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.ProductVersionListRestDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.ProductVersionRestDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.ProductVersionSummaryListRestDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.dtos.UpdateProductVersionInputRestDTO;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregatePrimaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductVersionRestMapper
    extends AggregatePrimaryMapper<ProductVersion, ProductVersionId, ProductVersionRestDTO> {

  public static final ProductVersionRestMapper INSTANCE =
      Mappers.getMapper(ProductVersionRestMapper.class);

  public abstract ProductVersionSummaryListRestDTO toSummaryDTO(
      PaginatedList<ProductVersionSummary> productVersions);

  public String toVersionItemDTO(ProductVersionSummary productVersion) {
    return productVersion.getVersionId().toString();
  }

  public abstract ProductVersionListRestDTO toVersionListDTO(
      PaginatedList<ProductVersionSummary> productVersions);

  public ProductVersionId toDomain(String productCode, String effectiveDate) {
    return new ProductVersionId(productCode + "::" + effectiveDate);
  }

  public SearchProductVersionsParams toSearchProductsParams(
      Integer pageNumber,
      Integer pageSize,
      ProductOrderRestDTO order,
      Boolean ascending,
      String code,
      OffsetDateTime searchDate) {
    var filter = toProductFilter(pageNumber, pageSize, order, ascending, code, searchDate);

    return new SearchProductVersionsParams(filter);
  }

  @Mapping(target = "minEffectiveDate", ignore = true)
  @Mapping(target = "maxEffectiveDate", ignore = true)
  protected abstract ProductVersionCriteria toProductFilter(
      Integer pageNumber,
      Integer pageSize,
      ProductOrderRestDTO order,
      Boolean ascending,
      String code,
      OffsetDateTime searchDate);

  public abstract CreateProductVersionParams toCreateProductParams(
      ProductVersionId versionId, CreateProductVersionInputRestDTO productInputRestDTO);

  public abstract GetProductVersionParams toGetProductParams(ProductVersionId versionId);

  @Mapping(target = "name", source = "inputRestDTO.name")
  public abstract UpdateProductVersionParams toUpdateProductParams(
      ProductVersionId versionId, UpdateProductVersionInputRestDTO inputRestDTO);

  public abstract PublishProductVersionParams toPublishProductParams(ProductVersionId versionId);

  public abstract UnpublishProductVersionParams toUnpublishProductParams(ProductVersionId versionId);

  public abstract DeleteProductVersionParams toDeleteProductParams(ProductVersionId versionId);
}
