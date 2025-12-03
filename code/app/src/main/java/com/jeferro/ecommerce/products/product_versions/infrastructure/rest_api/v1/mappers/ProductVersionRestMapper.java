package com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.v1.mappers;

import com.jeferro.ecommerce.products.product_versions.application.params.*;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api.v1.dtos.*;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregatePrimaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(config = MapstructConfig.class)
public abstract class ProductVersionRestMapper
        extends AggregatePrimaryMapper<ProductVersion, ProductVersionId, ProductVersionRestDTO> {

  public abstract ProductVersionSummaryListRestDTO toSummaryDTO(PaginatedList<ProductVersionSummary> productVersions);

  public String toVersionItemDTO(ProductVersionSummary productVersion) {
    return productVersion.getVersionId().toString();
  }

  public abstract ProductVersionIdListRestDTO toVersionListDTO(PaginatedList<ProductVersionSummary> productVersions);

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

  public abstract CreateProductVersionParams toCreateProductParams(ProductVersionId productVersionId,
                                                                   CreateProductVersionInputRestDTO createProductVersionInputRestDTO);

  public abstract GetProductVersionParams toGetProductParams(ProductVersionId productVersionId);

  @Mapping(target = "name", source = "updateProductVersionInputRestDTO.name")
  public abstract UpdateProductVersionParams toUpdateProductParams(ProductVersionId productVersionId,
                                                                   UpdateProductVersionInputRestDTO updateProductVersionInputRestDTO);

  public abstract PublishProductVersionParams toPublishProductParams(ProductVersionId productVersionId,
                                                                     PublishProductVersionInputRestDTO publishProductVersionInputRestDTO);

  public abstract UnpublishProductVersionParams toUnpublishProductParams(ProductVersionId productVersionId,
                                                                         UnpublishProductVersionInputRestDTO unpublishProductVersionInputRestDTO);

  public abstract DeleteProductVersionParams toDeleteProductParams(ProductVersionId productVersionId);
}
