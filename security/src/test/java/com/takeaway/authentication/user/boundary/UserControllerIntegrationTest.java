package com.takeaway.authentication.user.boundary;

import com.takeaway.authentication.IntegrationTestSuite;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.user.control.UserService;
import com.takeaway.authentication.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko Date: 21.10.2019 Time: 12:13
 *
 * <p>
 */
@AutoConfigureMockMvc
class UserControllerIntegrationTest extends IntegrationTestSuite
{
  @Autowired
  private MockMvc mockMvc;

  @Nested
  @DisplayName("when create")
  class WhenCreate
  {
    @Test
    @DisplayName("POST: 'http://.../users' returns CREATED if the creation request is valid")
    void givenValidCreateRequest_whenCreate_thenStatus201AndReturnNewEntity() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.createDefault();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      MvcResult mvcResult = mockMvc
              .perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson))
              .andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
              .andExpect(jsonPath("$", notNullValue()))
              .andExpect(header().string(HttpHeaders.LOCATION, containsString(UserController.BASE_URI)))
              .andReturn();

      String contentAsString = mvcResult.getResponse().getContentAsString();
      User created = objectMapper.readValue(contentAsString, User.class);
      assertThat(created).isNotNull();
      assertThat(created.getId()).isNotNull();
      assertThat(created.getUserName()).isEqualTo(toPersist.getUserName());
    }

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the password and  confirm password do not match")
    void givenNoMatchBetweenPasswordAndConfirmPassword_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.builder().newPassword(RandomStringUtils.randomAlphabetic(9)).confirmPassword(RandomStringUtils.randomAlphabetic(9)).create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the user name is null")
    void givenNullUserName_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.builder().userName(null).create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the user name is empty")
    void givenEmptyUserName_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.builder().userName("").create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the password is null")
    void givenNullPassword_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.builder().newPassword(null).create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the confirm password is null")
    void givenNullConfirmPassword_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User toPersist = userTestFactory.builder().confirmPassword(null).create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Autowired
  private UserService userService;

  private List<User> saveRandomUsers(int count) throws Exception
  {
    int normalizedCount = count <= 0 ? 1 : count;
    List<User> result = new ArrayList<>(count);
    for (int i = 0; i < normalizedCount; i++)
    {
      result.add(userService.create(userTestFactory.createDefault()));
    }

    return result;
  }
}
