package com.jeferro.products.users.infrastructure.rest_api;

import com.jeferro.products.shared.application.StubUseCaseBus;
import com.jeferro.products.shared.infrastructure.adapters.rest.RestControllerTest;
import com.jeferro.products.shared.infrastructure.adapters.utils.ApprovalUtils;
import com.jeferro.products.users.domain.models.UserMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AuthenticationsRestController.class)
class AuthenticationsRestControllerTest extends RestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private StubUseCaseBus stubUseCaseBus;

  @Test
  void execute_sign_in_on_request() throws Exception {
    var user = UserMother.john();
    stubUseCaseBus.init(user);

    var requestContent =
        """
                {
                  "username": "%s",
                  "password": "plain-password"
                }"""
            .formatted(user.getUsername());

    var requestBuilder =
        MockMvcRequestBuilders.post("/v1/authentications")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .content(requestContent);

    var response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    ApprovalUtils.verifyAll(
        stubUseCaseBus.getFirstParamsOrError(),
        response.getStatus(),
        response.getContentAsString());
  }
}
