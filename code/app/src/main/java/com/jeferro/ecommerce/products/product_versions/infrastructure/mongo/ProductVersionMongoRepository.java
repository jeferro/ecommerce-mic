package com.jeferro.ecommerce.products.product_versions.infrastructure.mongo;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.ecommerce.products.product_versions.domain.repositories.ProductVersionRepository;
import com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.daos.ProductVersionsMongoDao;
import com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.mappers.ProductVersionMongoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductVersionMongoRepository implements ProductVersionRepository {

  private final ProductVersionMongoMapper productVersionMongoMapper = ProductVersionMongoMapper.INSTANCE;

  private final ProductVersionsMongoDao productVersionsMongoDao;

  @Override
  public ProductVersion save(ProductVersion productVersion) {
	var dto = productVersionMongoMapper.toDTO(productVersion);

	var resultDto = productVersionsMongoDao.save(dto);

	return productVersionMongoMapper.toDomain(resultDto);
  }

  @Override
  public Optional<ProductVersion> findById(ProductVersionId versionId) {
	var versionIdDto = productVersionMongoMapper.toDTO(versionId);

	return productVersionsMongoDao.findById(versionIdDto).map(productVersionMongoMapper::toDomain);
  }

  @Override
  public void delete(ProductVersion version) {
	var versionDto = productVersionMongoMapper.toDTO(version);

	productVersionsMongoDao.delete(versionDto);
  }

  @Override
  public List<ProductVersion> findAll(ProductVersionCriteria criteria) {
	var page = productVersionsMongoDao.findAll(criteria);

	return productVersionMongoMapper.toDomain(page);
  }

  @Override
  public long count(ProductVersionCriteria criteria) {
	return productVersionsMongoDao.count(criteria);
  }

  @Override
  public List<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria) {
	var page = productVersionsMongoDao.findAll(criteria, ProductVersionSummaryMongoDTO.class, ProductVersionSummaryMongoDTO.FIELDS);

	return productVersionMongoMapper.toDomainSummary(page);
  }
}
