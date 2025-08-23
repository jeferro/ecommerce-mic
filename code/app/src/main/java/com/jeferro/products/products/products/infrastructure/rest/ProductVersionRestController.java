package com.jeferro.products.products.products.infrastructure.rest;

import com.jeferro.products.generated.rest.v1.apis.ProductVersionsApi;
import com.jeferro.products.generated.rest.v1.dtos.CreateProductVersionInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductFilterOrderRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductVersionListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductVersionRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductVersionSummaryListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateProductVersionInputRestDTO;
import com.jeferro.products.products.products.infrastructure.rest.mappers.ProductRestMapper;
import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class ProductVersionRestController implements ProductVersionsApi {

    private final ProductRestMapper productRestMapper = ProductRestMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @Override
    public ProductVersionSummaryListRestDTO searchProductVersions(Integer pageNumber, Integer pageSize, ProductFilterOrderRestDTO order,
        Boolean ascending, String name, OffsetDateTime searchDate) {
        var params = productRestMapper.toSearchProductsParams(pageNumber, pageSize, order, ascending, null, name, searchDate);

        var productVersions = useCaseBus.execute(params);

        return productRestMapper.toSummaryDTO(productVersions);
    }

    @Override
    public ProductVersionListRestDTO searchProductVersionIds(String productCode, Integer pageNumber, Integer pageSize) {
        var params = productRestMapper.toSearchProductsParams(pageNumber, pageSize, null, null,  productCode, null, null);

        var productVersions = useCaseBus.execute(params);

        return productRestMapper.toVersionListDTO(productVersions);
    }

    @Override
    public ProductVersionRestDTO createProductVersion(String productCode, String effectiveDate,
        CreateProductVersionInputRestDTO createProductVersionInputRestDTO) {
        var params = productRestMapper.toCreateProductParams(
            productRestMapper.toDomain(productCode, effectiveDate),
            createProductVersionInputRestDTO);

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }

    @Override
    public ProductVersionRestDTO getProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toGetProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }

    @Override
    public ProductVersionRestDTO updateProductVersion(String productCode, String effectiveDate,
        UpdateProductVersionInputRestDTO updateProductVersionInputRestDTO) {
        var params = productRestMapper.toUpdateProductParams(
            productRestMapper.toDomain(productCode, effectiveDate),
            updateProductVersionInputRestDTO);

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }

    @Override
    public ProductVersionRestDTO publishProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toPublishProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }

    @Override
    public ProductVersionRestDTO unpublishProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toUnpublishProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }

    @Override
    public ProductVersionRestDTO deleteProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toDeleteProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var productVersion = useCaseBus.execute(params);

        return productRestMapper.toDTO(productVersion);
    }
}
