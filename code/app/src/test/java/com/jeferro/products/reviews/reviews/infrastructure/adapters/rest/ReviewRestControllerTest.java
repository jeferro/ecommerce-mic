package com.jeferro.products.reviews.reviews.infrastructure.adapters.rest;

import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.infrastructure.rest.ReviewRestController;
import com.jeferro.products.products.products.domain.models.ProductVersionMother;
import com.jeferro.products.shared.application.StubUseCaseBus;
import com.jeferro.products.shared.infrastructure.adapters.rest.RestControllerTest;
import com.jeferro.products.shared.infrastructure.adapters.utils.ApprovalUtils;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ReviewRestController.class)
class ReviewRestControllerTest extends RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubUseCaseBus stubUseCaseBus;

    @Test
    void execute_search_reviews_on_request() throws Exception {
        var apple = ProductVersionMother.appleV1();
        var productReviews = PaginatedList.createOfItems(
            ReviewMother.johnReviewOfApple(),
            ReviewMother.emilyReviewOfApple()
        );
        stubUseCaseBus.init(productReviews);

        String url = "/v1/reviews?"
            + "domain=products"
            + "&id" + apple.getCode();

        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamsOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_create_product_on_request() throws Exception {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestContent = """
                {
                  "productCode": "%s",
                  "comment": "%s"
                }"""
                .formatted(johnReviewOfApple.getEntityId(), johnReviewOfApple.getComment());

        var requestBuilder = MockMvcRequestBuilders.post("/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
                .content(requestContent);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamsOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_get_product_on_request() throws Exception {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestBuilder = MockMvcRequestBuilders.get("/v1/reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamsOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_update_product_on_request() throws Exception {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestContent = """
                {
                  "comment": "%s"
                }"""
                .formatted(johnReviewOfApple.getComment());

        var requestBuilder = MockMvcRequestBuilders.patch("/v1/reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
                .content(requestContent);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamsOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_delete_product_on_request() throws Exception {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestBuilder = MockMvcRequestBuilders.delete("/v1/reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamsOrError(),
                response.getStatus(),
                response.getContentAsString());
    }
}
