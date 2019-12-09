package com.takeaway.authorization.rolepermission.boundary;

import com.takeaway.authorization.IntegrationTestSuite;
import com.takeaway.authorization.permission.control.PermissionService;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.runtime.security.boundary.AccessTokenParameter;
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
 * User: StMinko Date: 19.10.2019 Time: 17:47
 *
 * <p>
 */
@AutoConfigureMockMvc
class RolePermissionControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("when assign")
    class WhenAssign
    {
        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenAssign_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenAssign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                   "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                           .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns FORBIDDEN for missing role")
        void givenMissingRole_whenAssign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                   "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                           .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns CREATED and the assigned permission ")
        void givenRoleAndPermission_whenAssign_thenStatus200AndReturnPermission() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            Permission savedPermission = savePermission(permissionTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri, savedRole.getId(), savedPermission.getId()).header(HttpHeaders.AUTHORIZATION,
                                                                                                               "Bearer " + obtainAccessToken())
                                                                                                       .contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();

            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Permission response = objectMapper.readValue(contentAsString, Permission.class);
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(savedPermission.getId());
            assertThat(response.getName()).isEqualTo(savedPermission.getName());
            assertThat(response.getDescription()).isEqualTo(savedPermission.getDescription());
            assertThat(response.getCreatedAt()).isEqualTo(savedPermission.getCreatedAt());
            assertThat(response.getLastUpdatedAt()).isEqualTo(savedPermission.getLastUpdatedAt());
            assertThat(response.getCreatedBy()).isEqualTo(savedPermission.getCreatedBy());
            assertThat(response.getLastUpdatedBy()).isEqualTo(savedPermission.getLastUpdatedBy());
            assertThat(response.getVersion()).isEqualTo(savedPermission.getVersion());
        }

        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns BAD REQUEST on unknown permission ")
        void givenUnknownPermission_whenAssign_thenStatus404() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            UUID unknownPermissionId = UUID.randomUUID();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, savedRole.getId(), unknownPermissionId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Permission] for Id [%s]!", unknownPermissionId))));
        }

        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns BAD REQUEST on unknown role ")
        void givenUnknownRole_whenAssign_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownRoleId = UUID.randomUUID();
            Permission savedPermission = savePermission(permissionTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri, unknownRoleId, savedPermission.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Role] for Id [%s]!", unknownRoleId))));
        }
    }

    @Nested
    @DisplayName("when unassign")
    class WhenUnassign
    {
        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenUnassign_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenUnassign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                     "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns FORBIDDEN for missing role")
        void givenMissingRole_whenUnassign_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, UUID.randomUUID(), UUID.randomUUID()).header(HttpHeaders.AUTHORIZATION,
                                                                                     "Bearer " + obtainAccessToken(accessTokenParameter))
                                                                             .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns NO CONTENT ")
        void givenRoleAndPermission_whenUnassign_thenStatus204() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            Permission savedPermission = savePermission(permissionTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);
            mockMvc.perform(post(uri, savedRole.getId(), savedPermission.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                                 .contentType(APPLICATION_JSON_UTF8));

            // Act / Assert
            mockMvc.perform(delete(uri, savedRole.getId(), savedPermission.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                                   .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns BAD REQUEST on unknown permission ")
        void givenUnknownPermission_whenUnassign_thenStatus404() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            UUID unknownPermissionId = UUID.randomUUID();
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, savedRole.getId(), unknownPermissionId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                               .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Permission] for Id [%s]!", unknownPermissionId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{roleId}/permissions/{permissionId}' returns BAD REQUEST on unknown role ")
        void givenUnknownRole_whenUnassign_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownRoleId = UUID.randomUUID();
            Permission savedPermission = savePermission(permissionTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, unknownRoleId, savedPermission.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                               .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", is(String.format("Could not find [Role] for Id [%s]!", unknownRoleId))));
        }
    }

    private Role saveRole(Role role) throws Exception
    {
        return roleService.create(role);
    }

    private Permission savePermission(Permission permission) throws Exception
    {
        return permissionService.create(permission);
    }
}
