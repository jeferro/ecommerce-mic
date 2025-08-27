package com.jeferro.products.reviews.reviews.infrastructure.rest.mappers;

import com.jeferro.products.generated.rest.v1.dtos.CreateReviewInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewOrderRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateReviewInputRestDTO;
import com.jeferro.products.reviews.reviews.application.params.CreateReviewParams;
import com.jeferro.products.reviews.reviews.application.params.DeleteReviewParams;
import com.jeferro.products.reviews.reviews.application.params.GetReviewParams;
import com.jeferro.products.reviews.reviews.application.params.SearchReviewParams;
import com.jeferro.products.reviews.reviews.application.params.UpdateReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewFilter;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregateRestMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewRestMapper extends AggregateRestMapper<Review, ReviewId, ReviewRestDTO> {

    public static final ReviewRestMapper INSTANCE = Mappers.getMapper(ReviewRestMapper.class);

    public abstract ReviewListRestDTO toSummaryListDTO(PaginatedList<Review> reviews);

    public SearchReviewParams toSearchProductsParams(Integer pageNumber,
        Integer pageSize,
        ReviewOrderRestDTO order,
        Boolean ascending,
        String domain,
        String id) {
        var filter = toReviewFilter(pageNumber,
            pageSize,
            order,
            ascending,
            domain,
            id);

        return new SearchReviewParams(filter);
    }

    protected abstract ReviewFilter toReviewFilter(Integer pageNumber,
        Integer pageSize,
        ReviewOrderRestDTO order,
        Boolean ascending,
        String domain,
        String id);

    public abstract CreateReviewParams toCreateProductReviewParams(CreateReviewInputRestDTO inputRestDTO);

    public abstract GetReviewParams toGetProductReviewParams(String reviewId);

    public abstract UpdateReviewParams toUpdateProductReviewParams(String reviewId,
                                                                          UpdateReviewInputRestDTO inputRestDTO);

    public abstract DeleteReviewParams toDeleteProductReviewParams(String reviewId);
}
