package com.takeaway.authorization.userrole.boundary;

import com.takeaway.authorization.IntegrationTestSuite;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.runtime.security.boundary.AccessTokenParameter;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko Date: 30.10.2019 Time: 12:08
 *
 * <p>
 */
@AutoConfigureMockMvc
class UserRoleControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Nested
    @DisplayName("when assign")
    class WhenAssign
    {
        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId}' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenAssign_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId}' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenAssign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                   "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                           .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId} returns FORBIDDEN for missing role")
        void givenMissingRole_whenAssign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                   "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                           .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId}' returns CREATED and the assigned permission ")
        void givenRoleAndPermission_whenAssign_thenStatus200AndReturnRole() throws Exception
        {
            // Arrange
            User savedUser = saveUser(userTestFactory.createDefault());
            Role savedRole = saveRole(roleTestFactory.createDefault());
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri, savedUser.getId(), savedRole.getId()).header(HttpHeaders.AUTHORIZATION,
                                                                                                         "Bearer " + obtainAccessToken())
                                                                                                 .contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role response = objectMapper.readValue(contentAsString, Role.class);
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(savedRole.getId());
            assertThat(response.getName()).isEqualTo(savedRole.getName());
            assertThat(response.getDescription()).isEqualTo(savedRole.getDescription());
            assertThat(response.getCreatedAt()).isEqualTo(savedRole.getCreatedAt());
            assertThat(response.getLastUpdatedAt()).isEqualTo(savedRole.getLastUpdatedAt());
            assertThat(response.getCreatedBy()).isEqualTo(savedRole.getCreatedBy());
            assertThat(response.getLastUpdatedBy()).isEqualTo(savedRole.getLastUpdatedBy());
            assertThat(response.getVersion()).isEqualTo(savedRole.getVersion());
        }

        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId}' returns BAD REQUEST on unknown role ")
        void givenUnknownRole_whenAssign_thenStatus404() throws Exception
        {
            // Arrange
            User savedUser = saveUser(userTestFactory.createDefault());
            UUID unknownRoleId = UUID.randomUUID();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, savedUser.getId(), unknownRoleId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                       .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Role] for Id [%s]!", unknownRoleId))));
        }

        @Test
        @DisplayName("POST: 'http://.../users/{userId}/roles/{roleId}' returns BAD REQUEST on unknown user ")
        void givenUnknownUser_whenAssign_thenStatus404() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            UUID unknownUserId = UUID.randomUUID();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, unknownUserId, savedRole.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                       .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [User] for Id [%s]!", unknownUserId))));
        }
    }

    @Nested
    @DisplayName("when unassign")
    class WhenUnassign
    {
        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId}' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenUnassign_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId}' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenUnassign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                     "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId} returns FORBIDDEN for missing role")
        void givenMissingRole_whenUnassign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                     "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId}' returns NO CONTENT  ")
        void givenRoleAndPermission_whenUnassign_thenStatus204() throws Exception
        {
            // Arrange
            User savedUser = saveUser(userTestFactory.createDefault());
            Role savedRole = saveRole(roleTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", UserRoleController.BASE_URI);
            mockMvc.perform(post(uri, savedUser.getId(), savedRole.getId()).contentType(APPLICATION_JSON_UTF8));

            // Act / Assert
            mockMvc.perform(delete(uri, savedUser.getId(), savedRole.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId}' returns BAD REQUEST on unknown role ")
        void givenUnknownRole_whenUnassign_thenStatus404() throws Exception
        {
            // Arrange
            User savedUser = saveUser(userTestFactory.createDefault());
            UUID unknownRoleId = UUID.randomUUID();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, savedUser.getId(), unknownRoleId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                         .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Role] for Id [%s]!", unknownRoleId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../users/{userId}/roles/{roleId}' returns BAD REQUEST on unknown user ")
        void givenUnknownUser_whenUnassign_thenStatus404() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            UUID unknownUserId = UUID.randomUUID();
            String uri = String.format("%s/{roleId}", UserRoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, unknownUserId, savedRole.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                         .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [User] for Id [%s]!", unknownUserId))));
        }
    }

    private User saveUser(User user) throws Exception
    {
        return userService.create(user);
    }

    private Role saveRole(Role role) throws Exception
    {
        return roleService.create(role);
    }
}
