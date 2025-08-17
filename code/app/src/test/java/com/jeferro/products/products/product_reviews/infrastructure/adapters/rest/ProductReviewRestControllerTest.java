package com.jeferro.products.products.product_reviews.infrastructure.adapters.rest;

import com.jeferro.products.products.product_reviews.domain.models.ProductReviewMother;
import com.jeferro.products.products.product_reviews.infrastructure.rest.ProductReviewRestController;
import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.shared.application.StubUseCaseBus;
import com.jeferro.products.shared.infrastructure.adapters.rest.RestControllerTest;
import com.jeferro.products.shared.infrastructure.adapters.utils.ApprovalUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@WebMvcTest(ProductReviewRestController.class)
class ProductReviewRestControllerTest extends RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubUseCaseBus stubUseCaseBus;

    @Test
    void execute_list_product_reviews_on_request() throws Exception {
        var apple = ProductMother.appleV1();
        var productReviews = List.of(
            ProductReviewMother.johnReviewOfApple(),
            ProductReviewMother.emilyReviewOfApple()
        );
        stubUseCaseBus.init(productReviews);

        var requestBuilder = MockMvcRequestBuilders.get("/v1/product-reviews?productCode=" + apple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_create_product_on_request() throws Exception {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestContent = """
                {
                  "productCode": "%s",
                  "comment": "%s"
                }"""
                .formatted(johnReviewOfApple.getProductCode(), johnReviewOfApple.getComment());

        var requestBuilder = MockMvcRequestBuilders.post("/v1/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
                .content(requestContent);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_get_product_on_request() throws Exception {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestBuilder = MockMvcRequestBuilders.get("/v1/product-reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_update_product_on_request() throws Exception {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestContent = """
                {
                  "comment": "%s"
                }"""
                .formatted(johnReviewOfApple.getComment());

        var requestBuilder = MockMvcRequestBuilders.patch("/v1/product-reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
                .content(requestContent);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamOrError(),
                response.getStatus(),
                response.getContentAsString());
    }

    @Test
    void execute_delete_product_on_request() throws Exception {
        var johnReviewOfApple = ProductReviewMother.johnReviewOfApple();
        stubUseCaseBus.init(johnReviewOfApple);

        var requestBuilder = MockMvcRequestBuilders.delete("/v1/product-reviews/" + johnReviewOfApple.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

        var response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        ApprovalUtils.verifyAll(stubUseCaseBus.getFirstParamOrError(),
                response.getStatus(),
                response.getContentAsString());
    }
}
