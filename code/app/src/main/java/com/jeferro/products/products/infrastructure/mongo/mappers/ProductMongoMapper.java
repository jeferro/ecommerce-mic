package com.jeferro.products.products.infrastructure.mongo.mappers;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateMongoMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(config = MapstructConfig.class)
public abstract class ProductMongoMapper extends AggregateMongoMapper<ProductVersion, ProductVersionId, ProductVersionMongoDTO> {

    public static final ProductMongoMapper INSTANCE = Mappers.getMapper(ProductMongoMapper.class);

    public abstract ProductVersionSummary toDomain(ProductVersionSummaryMongoDTO dto);

    public PaginatedList<ProductVersionSummary> toDomainSummary(Page<ProductVersionSummaryMongoDTO> page) {
        var entities = page.getContent().stream()
            .map(this::toDomain)
            .toList();

        return new PaginatedList<>(entities, page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
