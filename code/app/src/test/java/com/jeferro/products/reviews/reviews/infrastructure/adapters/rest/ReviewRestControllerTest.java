package com.jeferro.products.reviews.reviews.infrastructure.adapters.rest;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewMother;
import com.jeferro.products.reviews.reviews.infrastructure.rest.ReviewRestController;
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
        Review johnReviewOfApple = ReviewMother.johnReviewOfApple();
        var productReviews = PaginatedList.createOfItems(
            johnReviewOfApple,
            ReviewMother.emilyReviewOfApple()
        );
        stubUseCaseBus.init(productReviews);

        String url = "/v1/reviews?"
            + "pageNumber=1"
            + "&pageSize=10"
            + "&entityId=" + johnReviewOfApple.getEntityId();

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
    void execute_create_review_on_request() throws Exception {
        var johnReviewOfApple = ReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestContent = """
                {
                  "entityId": "%s",
                  "comment": "%s"
                }"""
                .formatted(johnReviewOfApple.getId(), johnReviewOfApple.getComment());

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
    void execute_get_review_on_request() throws Exception {
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
    void execute_update_review_on_request() throws Exception {
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
    void execute_delete_review_on_request() throws Exception {
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
