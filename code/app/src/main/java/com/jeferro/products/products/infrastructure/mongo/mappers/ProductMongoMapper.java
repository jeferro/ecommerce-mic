package com.jeferro.products.products.infrastructure.mongo.mappers;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.shared.mappers.AggregateMongoMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductMongoMapper extends AggregateMongoMapper<ProductVersion, ProductVersionId, ProductVersionMongoDTO> {

    public static final ProductMongoMapper INSTANCE = Mappers.getMapper(ProductMongoMapper.class);
}
