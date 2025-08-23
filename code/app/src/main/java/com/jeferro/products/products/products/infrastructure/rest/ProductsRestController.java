package com.jeferro.products.products.products.infrastructure.rest;

import com.jeferro.products.generated.rest.v1.apis.ProductsApi;
import com.jeferro.products.generated.rest.v1.dtos.CreateProductInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductFilterOrderRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductSummaryListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateProductInputRestDTO;
import com.jeferro.products.products.products.infrastructure.rest.mappers.ProductRestMapper;
import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class ProductsRestController implements ProductsApi {

    private final ProductRestMapper productRestMapper = ProductRestMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @Override
    public ProductSummaryListRestDTO searchProducts(Integer pageNumber, Integer pageSize,
                                                      ProductFilterOrderRestDTO order,
                                                      Boolean ascending,
                                                      String name,
                                                      OffsetDateTime searchDate) {
        var params = productRestMapper.toSearchProductsParams(pageNumber, pageSize, order, ascending, name, searchDate);

        var products = useCaseBus.execute(params);

        return productRestMapper.toSummaryDTO(products);
    }

    @Override
    public ProductRestDTO createProduct(CreateProductInputRestDTO createProductInputRestDTO) {
        var params = productRestMapper.toCreateProductParams(createProductInputRestDTO);

        var product = useCaseBus.execute(params);

        return productRestMapper.toDTO(product);
    }

    @Override
    public ProductRestDTO getProduct(String productCode) {
        var params = productRestMapper.toGetProductParams(productCode);

        var product = useCaseBus.execute(params);

        return productRestMapper.toDTO(product);
    }

    @Override
    public ProductRestDTO updateProduct(String productCode, UpdateProductInputRestDTO updateProductInputRestDTO) {
        var params = productRestMapper.toUpdateProductParams(productCode, updateProductInputRestDTO);

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductRestDTO publishProduct(String productCode) {
        var params = productRestMapper.toPublishProductParams(productCode);

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductRestDTO unpublishProduct(String productCode) {
        var params = productRestMapper.toUnpublishProductParams(productCode);

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }

    @Override
    public ProductRestDTO deleteProduct(String productCode) {
        var params = productRestMapper.toDeleteProductParams(productCode);

        var user = useCaseBus.execute(params);

        return productRestMapper.toDTO(user);
    }
}
