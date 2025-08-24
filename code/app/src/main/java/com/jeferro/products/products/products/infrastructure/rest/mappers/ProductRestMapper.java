package com.jeferro.products.products.products.infrastructure.rest.mappers;

import com.jeferro.products.generated.rest.v1.dtos.*;
import com.jeferro.products.products.products.application.params.*;
import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.filter.ProductVersionFilter;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateRestMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

@Mapper(config = MapstructConfig.class)
public abstract class ProductRestMapper extends AggregateRestMapper<ProductVersion, ProductVersionId, ProductVersionRestDTO> {

    public static final ProductRestMapper INSTANCE = Mappers.getMapper(ProductRestMapper.class);

    public abstract ProductVersionSummaryListRestDTO toSummaryDTO(PaginatedList<ProductVersion> productVersions);

    public String toVersionItemDTO(ProductVersion productVersion) {
        return productVersion.getVersionId().getValue();
    }

    public abstract ProductVersionListRestDTO toVersionListDTO(PaginatedList<ProductVersion> productVersions);

    public ProductVersionId toDomain(String productCode, String effectiveDate) {
        return new ProductVersionId(productCode + "::" + effectiveDate);
    }

    public SearchProductsParams toSearchProductsParams(Integer pageNumber,
                                                       Integer pageSize,
                                                       ProductFilterOrderRestDTO order,
                                                       Boolean ascending,
                                                       String code,
                                                       OffsetDateTime searchDate) {
        var filter = toProductFilter(pageNumber, pageSize, order, ascending, code, searchDate);

        return new SearchProductsParams(filter);
    }

    @Mapping(target = "minEffectiveDate", ignore = true)
    @Mapping(target = "maxEffectiveDate", ignore = true)
    protected abstract ProductVersionFilter toProductFilter(Integer pageNumber,
                                                  Integer pageSize,
                                                  ProductFilterOrderRestDTO order,
                                                  Boolean ascending,
                                                  String code,
                                                  OffsetDateTime searchDate);

    public abstract CreateProductParams toCreateProductParams(ProductVersionId versionId, CreateProductVersionInputRestDTO productInputRestDTO);

    public abstract GetProductParams toGetProductParams(ProductVersionId versionId);

    @Mapping(target = "name", source = "inputRestDTO.name")
    public abstract UpdateProductParams toUpdateProductParams(ProductVersionId versionId, UpdateProductVersionInputRestDTO inputRestDTO);

    public abstract PublishProductParams toPublishProductParams(ProductVersionId versionId);

    public abstract UnpublishProductParams toUnpublishProductParams(ProductVersionId versionId);

    public abstract DeleteProductParams toDeleteProductParams(ProductVersionId versionId);
}
