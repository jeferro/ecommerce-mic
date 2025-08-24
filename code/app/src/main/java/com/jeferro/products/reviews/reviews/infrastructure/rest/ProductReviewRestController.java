package com.jeferro.products.reviews.reviews.infrastructure.rest;

import com.jeferro.products.generated.rest.v1.apis.ProductReviewsApi;
import com.jeferro.products.generated.rest.v1.dtos.CreateProductReviewInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductReviewListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductReviewRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateProductReviewInputRestDTO;
import com.jeferro.products.reviews.reviews.infrastructure.rest.mappers.ProductReviewRestMapper;
import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductReviewRestController implements ProductReviewsApi {

    private final ProductReviewRestMapper productReviewRestMapper = ProductReviewRestMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @Override
    public ProductReviewListRestDTO searchProductReviews(String productCode) {
        var params = productReviewRestMapper.toSearchProductsParams(productCode);

        var productReviews = useCaseBus.execute(params);

        return productReviewRestMapper.toSummaryListDTO(productReviews);
    }

    @Override
    public ProductReviewRestDTO createProductReview(CreateProductReviewInputRestDTO inputRestDTO) {
        var params = productReviewRestMapper.toCreateProductReviewParams(inputRestDTO);

        var productReview = useCaseBus.execute(params);

        return productReviewRestMapper.toDTO(productReview);
    }

    @Override
    public ProductReviewRestDTO getProductReview(String productReviewId) {
        var params = productReviewRestMapper.toGetProductReviewParams(productReviewId);

        var productReview = useCaseBus.execute(params);

        return productReviewRestMapper.toDTO(productReview);
    }

    @Override
    public ProductReviewRestDTO updateProductReview(String productReviewId,
                                                    UpdateProductReviewInputRestDTO inputRestDTO) {
        var params = productReviewRestMapper.toUpdateProductReviewParams(productReviewId, inputRestDTO);

        var productReview = useCaseBus.execute(params);

        return productReviewRestMapper.toDTO(productReview);
    }

    @Override
    public ProductReviewRestDTO deleteProductReview(String productReviewId) {
        var params = productReviewRestMapper.toDeleteProductReviewParams(productReviewId);

        var productReview = useCaseBus.execute(params);

        return productReviewRestMapper.toDTO(productReview);
    }
}
