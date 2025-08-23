package com.jeferro.products.products.product_reviews.application;

import com.jeferro.products.products.product_reviews.application.params.CreateProductReviewParams;
import com.jeferro.products.products.product_reviews.domain.events.ProductReviewCreated;
import com.jeferro.products.products.product_reviews.domain.exceptions.ProductReviewAlreadyExistsException;
import com.jeferro.products.products.product_reviews.domain.models.ProductReview;
import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.domain.repositories.ProductReviewsInMemoryRepository;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.products.products.products.domain.models.ProductCodeMother;
import com.jeferro.products.products.products.domain.repositories.ProductVersionInMemoryRepository;
import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.shared.domain.events.EventInMemoryBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CreateProductVersionReviewUseCaseTest {

  private ProductReviewsInMemoryRepository productReviewsInMemoryRepository;

    private EventInMemoryBus eventInMemoryBus;

    private CreateProductReviewUseCase createProductReviewUseCase;

    @BeforeEach
    public void beforeEach() {
	  ProductVersionInMemoryRepository productsInMemoryRepository = new ProductVersionInMemoryRepository();
        productReviewsInMemoryRepository = new ProductReviewsInMemoryRepository();
        eventInMemoryBus = new EventInMemoryBus();

        createProductReviewUseCase =
                new CreateProductReviewUseCase(productsInMemoryRepository, productReviewsInMemoryRepository, eventInMemoryBus);
    }

    @Test
    void givenUserDidNotCommentOnProduct_whenCreateProductReview_thenReturnsNewProductReview() {
        var productCode = ProductCodeMother.apple();
        var comment = "New comment about product";
        var params = new CreateProductReviewParams(
                productCode,
                comment
        );

        var jamesContext = ContextMother.james();
        var result = createProductReviewUseCase.execute(jamesContext, params);

        assertResult(jamesContext, result, productCode, comment);

        assertProductReviewInDatabase(result);

        assertProductReviewCreatedWasPublished(result);
    }

    @Test
    void givenUserCommentsOnProduct_whenCreateProductReview_throwsException() {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();

        var params = new CreateProductReviewParams(
                johnReviewOfApple.getProductCode(),
                "other comment"
        );

        assertThrows(ProductReviewAlreadyExistsException.class,
                () -> createProductReviewUseCase.execute(
                    ContextMother.john(),
                    params));
    }

    private static void assertResult(Context context, ProductReview result, ProductCode productCode, String comment) {
        assertEquals(context.getAuth().username(), result.getUsername());
        assertEquals(productCode, result.getProductCode());
        assertEquals(comment, result.getComment());
    }

    private void assertProductReviewInDatabase(ProductReview result) {
        assertTrue(productReviewsInMemoryRepository.contains(result));
    }

    private void assertProductReviewCreatedWasPublished(ProductReview result) {
        var event = eventInMemoryBus.filterOfClass(ProductReviewCreated.class)
            .findFirst();

        if(event.isEmpty()){
          fail();
        }

        assertEquals(result.getId(), event.get().getProductReviewId());
    }
}
