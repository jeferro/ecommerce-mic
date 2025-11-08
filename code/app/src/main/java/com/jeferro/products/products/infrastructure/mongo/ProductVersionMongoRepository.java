package com.jeferro.products.products.infrastructure.mongo;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.domain.models.criteria.ProductVersionCriteria;
import com.jeferro.products.products.domain.repositories.ProductVersionRepository;
import com.jeferro.products.products.infrastructure.mongo.daos.ProductVersionsMongoDao;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.products.products.infrastructure.mongo.mappers.ProductMongoMapper;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductVersionMongoRepository implements ProductVersionRepository {

  private final ProductMongoMapper productMongoMapper = ProductMongoMapper.INSTANCE;

  private final ProductVersionsMongoDao productVersionsMongoDao;

  @Override
  public void save(ProductVersion productVersion) {
    var dto = productMongoMapper.toDTO(productVersion);

    productVersionsMongoDao.save(dto);
  }

  @Override
  public Optional<ProductVersion> findById(ProductVersionId versionId) {
    var versionIdDto = productMongoMapper.toDTO(versionId);

    return productVersionsMongoDao.findById(versionIdDto).map(productMongoMapper::toDomain);
  }

  @Override
  public void delete(ProductVersion version) {
    var versionDto = productMongoMapper.toDTO(version);

    productVersionsMongoDao.delete(versionDto);
  }

  @Override
  public List<ProductVersion> findAll(ProductVersionCriteria criteria) {
    var page = productVersionsMongoDao.findAll(criteria);

    return productMongoMapper.toDomain(page);
  }

	@Override
	public long count(ProductVersionCriteria criteria) {
		return productVersionsMongoDao.count(criteria);
	}

	@Override
  public List<ProductVersionSummary> findAllSummary(ProductVersionCriteria criteria) {
    var page = productVersionsMongoDao.findAll(criteria, ProductVersionSummaryMongoDTO.class, ProductVersionSummaryMongoDTO.FIELDS);

    return productMongoMapper.toDomainSummary(page);
  }
}
