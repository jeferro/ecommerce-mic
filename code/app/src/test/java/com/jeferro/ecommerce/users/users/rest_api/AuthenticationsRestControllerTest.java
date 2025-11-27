package com.jeferro.ecommerce.users.users.rest_api;

import com.jeferro.ecommerce.shared.application.StubUseCaseBus;
import com.jeferro.ecommerce.shared.infrastructure.adapters.rest.RestControllerTest;
import com.jeferro.ecommerce.users.users.application.params.SignInParams;
import com.jeferro.ecommerce.users.users.domain.models.UserMother;
import com.jeferro.ecommerce.users.users.infrastructure.rest_api.AuthenticationsRestController;
import com.jeferro.ecommerce.users.users.infrastructure.rest_api.mappers.AuthRestMapper;
import com.jeferro.ecommerce.users.users.infrastructure.rest_api.mappers.AuthRestMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@WebMvcTest(AuthenticationsRestController.class)
@Import(AuthRestMapperImpl.class)
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

    var requestBuilder = MockMvcRequestBuilders.post("/v1/authentications")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_EN)
            .content(requestContent);

    var response = mockMvc.perform(requestBuilder)
        .andReturn()
        .getResponse();

    assertEquals(200, response.getStatus());

    var params = (SignInParams) stubUseCaseBus.getFirstParamsOrError();
    assertInstanceOf(SignInParams.class, params);
    assertEquals(user.getUsername(), params.getUsername());
    assertEquals("plain-password", params.getPassword());
  }
}
