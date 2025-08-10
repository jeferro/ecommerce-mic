package com.jeferro.products.products.products.infrastructure.adapters.rest;

import com.jeferro.products.products.products.domain.models.ProductMother;
import com.jeferro.products.products.products.infrastructure.rest.ProductsRestController;
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

@WebMvcTest(ProductsRestController.class)
class ProductsRestControllerTest extends RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubUseCaseBus stubUseCaseBus;

    @Test
    void execute_list_products_on_request() throws Exception {
        var products = PaginatedList.createOfItems(
                ProductMother.apple(),
                ProductMother.pear()
        );
        stubUseCaseBus.init(products);

        var requestBuilder = MockMvcRequestBuilders.get("/v1/products?pageNumber=0&pageSize=10&&order=NAME&ascending=true&name=apple")
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
        var apple = ProductMother.apple();
        stubUseCaseBus.init(apple);

        var requestContent = """
                {
                  "code": "%s",
                  "typeId": "%s",
                  "name": {
                    "en-US": "Apple"
                  }
                }"""
                .formatted(apple.getCode(), apple.getTypeId());

        var requestBuilder = MockMvcRequestBuilders.post("/v1/products")
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
        var apple = ProductMother.apple();
        stubUseCaseBus.init(apple);

        var requestBuilder = MockMvcRequestBuilders.get("/v1/products/" + apple.getId())
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
        var apple = ProductMother.apple();
        stubUseCaseBus.init(apple);

        var requestContent = """
                {
                  "name": {
                    "en-US": "%s"
                  }
                }"""
                .formatted(apple.getName());

        var requestBuilder = MockMvcRequestBuilders.patch("/v1/products/" + apple.getId())
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
        var apple = ProductMother.apple();
        stubUseCaseBus.init(apple);

        var requestBuilder = MockMvcRequestBuilders.delete("/v1/products/" + apple.getId())
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
