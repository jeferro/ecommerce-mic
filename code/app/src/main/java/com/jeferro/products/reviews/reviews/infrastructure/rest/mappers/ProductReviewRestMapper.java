package com.jeferro.products.reviews.reviews.infrastructure.rest.mappers;

import com.jeferro.products.generated.rest.v1.dtos.CreateProductReviewInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductReviewListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ProductReviewRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateProductReviewInputRestDTO;
import com.jeferro.products.reviews.reviews.application.params.CreateProductReviewParams;
import com.jeferro.products.reviews.reviews.application.params.DeleteProductReviewParams;
import com.jeferro.products.reviews.reviews.application.params.GetProductReviewParams;
import com.jeferro.products.reviews.reviews.application.params.SearchProductReviewParams;
import com.jeferro.products.reviews.reviews.application.params.UpdateProductReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateRestMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductReviewRestMapper extends AggregateRestMapper<ProductReview, ProductReviewId, ProductReviewRestDTO> {

    public static final ProductReviewRestMapper INSTANCE = Mappers.getMapper(ProductReviewRestMapper.class);

    public abstract ProductReviewListRestDTO toSummaryListDTO(PaginatedList<ProductReview> productReviews);

    public SearchProductReviewParams toSearchProductsParams(String productCodeRestDTO) {
        var productCode = new ProductCode(productCodeRestDTO);
        return new SearchProductReviewParams(productCode);
    }

    public abstract CreateProductReviewParams toCreateProductReviewParams(CreateProductReviewInputRestDTO inputRestDTO);

    public abstract GetProductReviewParams toGetProductReviewParams(String productReviewId);

    public abstract UpdateProductReviewParams toUpdateProductReviewParams(String productReviewId,
                                                                          UpdateProductReviewInputRestDTO inputRestDTO);

    public abstract DeleteProductReviewParams toDeleteProductReviewParams(String productReviewId);
}
