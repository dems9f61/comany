package com.takeaway.authentication.user.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authentication.IntegrationTestSuite;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
  @DisplayName("when access")
  class WhenAccess
  {
    @Test
    @DisplayName("GET: 'http://.../users' returns OK and an list of all users ")
    void givenRoles_whenFindAll_thenStatus200AndReturnFullList() throws Exception
    {
      // Arrange
      List<User> savedUsers = saveRandomUsers(4);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      MvcResult mvcResult = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andExpect(status().isOk())
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andExpect(jsonPath("$", notNullValue()))
                                   .andReturn();
      String contentAsString = mvcResult.getResponse()
                                        .getContentAsString();
      ApiResponsePage<User> apiResponsePage = objectMapper.readValue(contentAsString, new TypeReference<ApiResponsePage<User>>() {});
      assertThat(apiResponsePage).isNotNull();
      assertThat(apiResponsePage.getTotalElements()).isEqualTo(savedUsers.size());
      assertThat(savedUsers.stream()
                           .allMatch(savedUser -> apiResponsePage.stream()
                                                                 .anyMatch(role -> role.getId() != null && role.getId()
                                                                                                               .equals(savedUser.getId())))).isTrue();
    }

    @Test
    @DisplayName("GET: 'http://.../users/{id}' returns BAD REQUEST for unknown id ")
    void givenUnknownId_whenFindById_thenStatus404() throws Exception
    {
      // Arrange
      UUID unknownId = UUID.randomUUID();
      String uri = String.format("%s/{id}", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(get(uri, unknownId).contentType(MediaType.APPLICATION_JSON_UTF8))
             .andExpect(status().isNotFound())
             .andExpect(jsonPath("$", notNullValue()))
             .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
    }

    @Test
    @DisplayName("GET: 'http://.../users/{id}' returns OK and the requested permission")
    void givenUser_whenFindById_thenStatus200AndReturnRole() throws Exception
    {
      // Arrange
      User persistedUser = saveRandomUsers(1).get(0);
      String uri = String.format("%s/{id}", UserController.BASE_URI);

      // Act / Assert
      MvcResult mvcResult = mockMvc.perform(get(uri, persistedUser.getId()).contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andExpect(status().isOk())
                                   .andExpect(jsonPath("$", notNullValue()))
                                   .andReturn();
      String contentAsString = mvcResult.getResponse()
                                        .getContentAsString();
      User user = objectMapper.readValue(contentAsString, User.class);
      assertThat(user).isNotNull();
      assertThat(user.getId()).isEqualTo(persistedUser.getId());
      assertThat(user.getUserName()).isEqualTo(persistedUser.getUserName());
    }
  }

  @Nested
  @DisplayName("when update")
  class WhenUpdate
  {
    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns OK on valid full request")
    void givenValidFullRequest_whenDoFullUpdate_thenStatus200AndReturnUpdated() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.createDefault();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      MvcResult mvcResult = mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                                     .content(requestAsJson))
                                   .andExpect(status().isOk())
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                   .andExpect(jsonPath("$", notNullValue()))
                                   .andReturn();
      String contentAsString = mvcResult.getResponse()
                                        .getContentAsString();
      User updated = objectMapper.readValue(contentAsString, User.class);
      assertThat(updated).isNotNull();
      assertThat(updated.getId()).isEqualTo(initial.getId());
      assertThat(updated.getUserName()).isEqualTo(update.getUserName());
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on null user name")
    void givenNullUserName_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .userName(null)
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on empty name")
    void givenBlankUserName_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .userName("   ")
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on empty confirm password")
    void givenBlankConfirmPassword_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .confirmPassword("   ")
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on null confirm password")
    void givenNullConfirmPassword_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .confirmPassword(null)
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on discrepancy between old password and existing password")
    void givenOldPasswordDifferentFromExistingPassword_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .oldPassword(RandomStringUtils.randomAlphabetic(8))
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("PUT: 'http://.../users/{id}' returns BAD REQUEST on discrepancy between  password and confirm password")
    void givenPasswordDifferentFromConfirmPassword_whenDoFullUpdate_thenStatus400() throws Exception
    {
      // Arrange
      User initial = saveRandomUsers(1).get(0);
      User update = userTestFactory.builder()
                                   .confirmPassword(RandomStringUtils.randomAlphabetic(8))
                                   .create();
      String uri = String.format("%s/{id}", UserController.BASE_URI);
      String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

      // Act / Assert
      mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }
  }

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

    @Test
    @DisplayName("POST: 'http://.../users' returns BAD_REQUEST if the given user name already exists")
    void giveAlreadyExistingUserName_whenCreate_thenStatus400() throws Exception
    {
      // Arrange
      User persistedUser = saveRandomUsers(1).get(0);
      User toPersist = userTestFactory.builder()
                                      .userName(persistedUser.getUserName())
                                      .create();
      String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
      String uri = String.format("%s", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                               .content(requestAsJson))
             .andExpect(status().isBadRequest())
             .andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Nested
  @DisplayName("when delete")
  class WhenDelete
  {
    @Test
    @DisplayName("DELETE: 'http://.../users/{id}' returns NOT FOUND if the specified id doesn't exist")
    void givenUnknownUuid_whenDeleteById_thenStatus404() throws Exception
    {
      // Arrange
      UUID unknownId = UUID.randomUUID();
      String uri = String.format("%s/{id}", UserController.BASE_URI);

      mockMvc.perform(delete(uri, unknownId).contentType(APPLICATION_JSON_UTF8))
             .andExpect(status().isNotFound())
             .andExpect(jsonPath("$", notNullValue()))
             .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
    }

    @Test
    @DisplayName("DELETE: 'http://.../users/{id}' returns NO CONTENT if the specified id exists")
    void givenUser_whenDeleteById_thenStatus204() throws Exception
    {
      // Arrange
      User toDelete = saveRandomUsers(1).get(0);
      String uri = String.format("%s/{id}", UserController.BASE_URI);

      // Act / Assert
      mockMvc.perform(delete(uri, toDelete.getId()).contentType(APPLICATION_JSON_UTF8))
             .andExpect(status().isNoContent());
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
