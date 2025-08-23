package com.jeferro.products.products.products.infrastructure.mongo;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.products.domain.models.filter.ProductFilter;
import com.jeferro.products.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.products.products.products.infrastructure.mongo.daos.ProductsMongoDao;
import com.jeferro.products.products.products.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.products.products.products.infrastructure.mongo.mappers.ProductMongoMapper;
import com.jeferro.products.products.products.infrastructure.mongo.services.ProductQueryMongoCreator;
import com.jeferro.shared.auth.infrastructure.mongo.services.CustomMongoTemplate;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductVersionMongoRepository implements ProductVersionRepository {

    private final ProductMongoMapper productMongoMapper = ProductMongoMapper.INSTANCE;

    private final ProductsMongoDao productsMongoDao;

    private final ProductQueryMongoCreator productQueryMongoCreator;

    private final CustomMongoTemplate customMongoTemplate;

    @Override
    public void save(ProductVersion productVersion) {
        var dto = productMongoMapper.toDTO(productVersion);

        productsMongoDao.save(dto);
    }

    @Override
    public Optional<ProductVersion> findById(ProductVersionId versionId) {
        var versionIdDto = productMongoMapper.toDTO(versionId);

        return productsMongoDao.findById(versionIdDto)
                .map(productMongoMapper::toDomain);
    }

    @Override
    public void deleteById(ProductVersionId versionId) {
        var versionIdDto = productMongoMapper.toDTO(versionId);

        productsMongoDao.deleteById(versionIdDto);
    }

    @Override
    public PaginatedList<ProductVersion> findAll(ProductFilter filter) {
        Query query = productQueryMongoCreator.create(filter);

        Page<ProductVersionMongoDTO> page = customMongoTemplate.findPage(query, ProductVersionMongoDTO.class);

        List<ProductVersion> entities = page.getContent().stream()
                .map(productMongoMapper::toDomain)
                .toList();

        return PaginatedList.createOfFilter(entities, filter, page.getTotalElements());
    }
}
