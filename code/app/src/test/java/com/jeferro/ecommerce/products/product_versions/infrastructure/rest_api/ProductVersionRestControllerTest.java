package com.jeferro.ecommerce.products.product_versions.infrastructure.rest_api;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionMother;
import com.jeferro.ecommerce.shared.application.StubUseCaseBus;
import com.jeferro.ecommerce.shared.infrastructure.adapters.rest.RestControllerTest;
import com.jeferro.ecommerce.shared.utils.ApprovalUtils;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ProductVersionRestController.class)
class ProductVersionRestControllerTest extends RestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private StubUseCaseBus stubUseCaseBus;

  @Test
  void execute_list_product_versions_on_request() throws Exception {
    var products =
        PaginatedList.createOfItems(ProductVersionMother.appleV1(), ProductVersionMother.pearV1());
    stubUseCaseBus.init(products);

    var url =
        "/v1/products?"
            + "pageNumber=0"
            + "&pageSize=10"
            + "&order=NAME"
            + "&ascending=true"
            + "&name=apple";

    var requestBuilder =
        MockMvcRequestBuilders.get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }

  @Test
  void execute_list_product_version_ids_on_request() throws Exception {
    var appleV1 = ProductVersionMother.appleV1();

    var products =
        PaginatedList.createOfItems(ProductVersionMother.appleV1(), ProductVersionMother.appleV2());
    stubUseCaseBus.init(products);

    String url = "/v1/products/" + appleV1.getCode() + "/versions?pageNumber=0&pageSize=10";

    var requestBuilder =
        MockMvcRequestBuilders.get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }

  @Test
  void execute_create_product_version_on_request() throws Exception {
    var appleV1 = ProductVersionMother.appleV1();
    stubUseCaseBus.init(appleV1);

    var requestContent =
        """
                {
                  "versionId": "%s",
                  "typeId": "%s",
                  "name": {
                    "en-US": "Apple",
                    "es-ES": "Manzana"
                  }
                }"""
            .formatted(appleV1.getVersionId(), appleV1.getTypeId());

    var url = "/v1/products/" + appleV1.getCode() + "/versions/" + appleV1.getEffectiveDate();

    var requestBuilder =
        MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
            .content(requestContent);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }

  @Test
  void execute_get_product_version_on_request() throws Exception {
    var appleV1 = ProductVersionMother.appleV1();
    stubUseCaseBus.init(appleV1);

    var url = "/v1/products/" + appleV1.getCode() + "/versions/" + appleV1.getEffectiveDate();

    var requestBuilder =
        MockMvcRequestBuilders.get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }

  @Test
  void execute_update_product_version_on_request() throws Exception {
    var appleV1 = ProductVersionMother.appleV1();
    stubUseCaseBus.init(appleV1);

    var requestContent =
        """
                {
                  "name": {
                    "en-US": "%s"
                  }
                }"""
            .formatted(appleV1.getName());

    var url = "/v1/products/" + appleV1.getCode() + "/versions/" + appleV1.getEffectiveDate();

    var requestBuilder =
        MockMvcRequestBuilders.patch(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN)
            .content(requestContent);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }

  @Test
  void execute_delete_product_version_on_request() throws Exception {
    var appleV1 = ProductVersionMother.appleV1();
    stubUseCaseBus.init(appleV1);

    var url = "/v1/products/" + appleV1.getCode() + "/versions/" + appleV1.getEffectiveDate();

    var requestBuilder =
        MockMvcRequestBuilders.delete(url)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_USER_TOKEN);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }
}
