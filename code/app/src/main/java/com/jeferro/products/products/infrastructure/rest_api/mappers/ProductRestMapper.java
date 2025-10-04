package com.jeferro.products.products.infrastructure.rest_api.mappers;

import com.jeferro.products.products.application.params.CreateProductParams;
import com.jeferro.products.products.application.params.DeleteProductParams;
import com.jeferro.products.products.application.params.GetProductParams;
import com.jeferro.products.products.application.params.PublishProductParams;
import com.jeferro.products.products.application.params.SearchProductsParams;
import com.jeferro.products.products.application.params.UnpublishProductParams;
import com.jeferro.products.products.application.params.UpdateProductParams;
import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.infrastructure.rest_api.dtos.CreateProductVersionInputRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductOrderRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionListRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.ProductVersionSummaryListRestDTO;
import com.jeferro.products.products.infrastructure.rest_api.dtos.UpdateProductVersionInputRestDTO;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateRestMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductRestMapper
    extends AggregateRestMapper<ProductVersion, ProductVersionId, ProductVersionRestDTO> {

  public static final ProductRestMapper INSTANCE = Mappers.getMapper(ProductRestMapper.class);

  public abstract ProductVersionSummaryListRestDTO toSummaryDTO(
      PaginatedList<ProductVersionSummary> productVersions);

  public String toVersionItemDTO(ProductVersionSummary productVersion) {
    return productVersion.getVersionId().getValue();
  }

  public abstract ProductVersionListRestDTO toVersionListDTO(
      PaginatedList<ProductVersionSummary> productVersions);

  public ProductVersionId toDomain(String productCode, String effectiveDate) {
    return new ProductVersionId(productCode + "::" + effectiveDate);
  }

  public SearchProductsParams toSearchProductsParams(
      Integer pageNumber,
      Integer pageSize,
      ProductOrderRestDTO order,
      Boolean ascending,
      String code,
      OffsetDateTime searchDate) {
    var filter = toProductFilter(pageNumber, pageSize, order, ascending, code, searchDate);

    return new SearchProductsParams(filter);
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

  public abstract CreateProductParams toCreateProductParams(
      ProductVersionId versionId, CreateProductVersionInputRestDTO productInputRestDTO);

  public abstract GetProductParams toGetProductParams(ProductVersionId versionId);

  @Mapping(target = "name", source = "inputRestDTO.name")
  public abstract UpdateProductParams toUpdateProductParams(
      ProductVersionId versionId, UpdateProductVersionInputRestDTO inputRestDTO);

  public abstract PublishProductParams toPublishProductParams(ProductVersionId versionId);

  public abstract UnpublishProductParams toUnpublishProductParams(ProductVersionId versionId);

  public abstract DeleteProductParams toDeleteProductParams(ProductVersionId versionId);
}
