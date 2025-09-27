package com.jeferro.products.products.infrastructure.mongo;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.products.products.infrastructure.mongo.daos.ProductsMongoDao;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.products.products.infrastructure.mongo.mappers.ProductMongoMapper;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductVersionMongoRepository implements ProductVersionRepository {

    private final ProductMongoMapper productMongoMapper = ProductMongoMapper.INSTANCE;

    private final ProductsMongoDao productsMongoDao;

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
    public PaginatedList<ProductVersion> findAll(ProductVersionCriteria criteria) {
        var page = productsMongoDao.findAllByCriteria(criteria);

        return productMongoMapper.toDomain(page);
    }

    @Override
    public PaginatedList<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria) {
        var page = productsMongoDao.findAllByCriteria(criteria,
            ProductVersionSummaryMongoDTO.class,
            List.of("name", "status"));

        return productMongoMapper.toDomainSummary(page);
    }
}
