package com.jeferro.products.products.products.infrastructure.rest;

import com.jeferro.products.generated.rest.v1.apis.ProductVersionsApi;
import com.jeferro.products.generated.rest.v1.dtos.CreateProductVersionInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductFilterOrderRestDTO;
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
public class ProductsRestController implements ProductVersionsApi {

    private final ProductRestMapper productRestMapper = ProductRestMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @Override
    public ProductVersionSummaryListRestDTO searchProductVersions(Integer pageNumber, Integer pageSize, ProductFilterOrderRestDTO order,
        Boolean ascending, String name, OffsetDateTime searchDate) {
        var params = productRestMapper.toSearchProductsParams(pageNumber, pageSize, order, ascending, name, searchDate);

        var products = useCaseBus.execute(params);

        return productRestMapper.toSummaryDTO(products);
    }

    @Override
    public ProductVersionRestDTO createProductVersion(String productCode, String effectiveDate,
        CreateProductVersionInputRestDTO createProductVersionInputRestDTO) {
        var params = productRestMapper.toCreateProductParams(
            productRestMapper.toDomain(productCode, effectiveDate),
            createProductVersionInputRestDTO);

        var product = useCaseBus.execute(params);

        return productRestMapper.toDTO(product);
    }

    @Override
    public ProductVersionRestDTO getProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toGetProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var product = useCaseBus.execute(params);

        return productRestMapper.toDTO(product);
    }

    @Override
    public ProductVersionRestDTO updateProductVersion(String productCode, String effectiveDate,
        UpdateProductVersionInputRestDTO updateProductVersionInputRestDTO) {
        var params = productRestMapper.toUpdateProductParams(
            productRestMapper.toDomain(productCode, effectiveDate),
            updateProductVersionInputRestDTO);

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductVersionRestDTO publishProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toPublishProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductVersionRestDTO unpublishProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toUnpublishProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductVersionRestDTO deleteProductVersion(String productCode, String effectiveDate) {
        var params = productRestMapper.toDeleteProductParams(
            productRestMapper.toDomain(productCode, effectiveDate)
        );

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }
}
