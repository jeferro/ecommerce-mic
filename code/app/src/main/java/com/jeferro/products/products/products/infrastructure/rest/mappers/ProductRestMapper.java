package com.jeferro.products.products.products.infrastructure.rest.mappers;

import com.jeferro.products.generated.rest.v1.dtos.*;
import com.jeferro.products.products.products.application.params.*;
import com.jeferro.products.products.products.domain.models.Product;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.models.ProductId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateRestMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public abstract d class ProductRestMapper extends AggregateRestMapper<Product, ProductId, ProductRestDTO> {

    public static final ProductRestMapper INSTANCE = Mappers.getMapper(ProductRestMapper.class);

    public abstract ProductSummaryRestDTO toSummaryDTO(Product product);

    public abstract ProductSummaryListRestDTO toSummaryDTO(PaginatedList<Product> product);

    public SearchProductsParams toSearchProductsParams(Integer pageNumber,
                                                       Integer pageSize,
                                                       ProductFilterOrderRestDTO order,
                                                       Boolean ascending,
                                                       String name) {
        var filter = toProductFilter(pageNumber, pageSize, order, ascending, name);

        return new SearchProductsParams(filter);
    }

    protected abstract ProductFilter toProductFilter(Integer pageNumber,
                                                  Integer pageSize,
                                                  ProductFilterOrderRestDTO order,
                                                  Boolean ascending,
                                                  String name);

    public abstract CreateProductParams toCreateProductParams(CreateProductInputRestDTO productInputRestDTO);

    public abstract GetProductParams toGetProductParams(String productCode);

    @Mapping(target = "name", source = "inputRestDTO.name")
    public abstract UpdateProductParams toUpdateProductParams(String productCode, UpdateProductInputRestDTO inputRestDTO);

    public abstract PublishProductParams toPublishProductParams(String productCode);

    public abstract UnpublishProductParams toUnpublishProductParams(String productCode);

    public abstract DeleteProductParams toDeleteProductParams(String productCode);
}
