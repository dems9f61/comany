package com.takeaway.authentication.rolepermission.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authentication.IntegrationTestSuite;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.entity.AbstractEntity;
import com.takeaway.authentication.permission.control.PermissionService;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.role.control.RoleService;
import com.takeaway.authentication.role.entity.Role;
import com.takeaway.authentication.rolepermission.control.RolePermissionService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko
 * Date: 19.10.2019
 * Time: 17:47
 * <p/>
 */
@AutoConfigureMockMvc
class RolePermissionControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../roles/{roleId}/permissions' returns OK and an list of all role-permissions ")
        void givenRolePermissions_whenFindAll_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            List<Permission> permissionRequests = permissionTestFactory.createManyDefault(RandomUtils.nextInt(1, 5));
            List<Permission> savedPermissions = new ArrayList<>(permissionRequests.size());
            for (Permission permission : permissionRequests)
            {
                savedPermissions.add(savePermission(permission));
            }
            List<UUID> savedPermissionIds = savedPermissions.stream()
                                                            .map(AbstractEntity::getId)
                                                            .collect(Collectors.toList());
            UUID savedRoleId = savedRole.getId();
            savedPermissions = saveRolePermissionFor(savedRoleId, savedPermissionIds);
            String uri = String.format("%s", RolePermissionController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri, savedRoleId).contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            ApiResponsePage<Permission> apiResponsePage = objectMapper.readValue(contentAsString,
                                                                                 new TypeReference<ApiResponsePage<Permission>>() {});
            assertThat(apiResponsePage).isNotNull();
            assertThat(apiResponsePage.getTotalElements()).isEqualTo(permissionRequests.size());
            assertThat(savedPermissions.stream()
                                       .allMatch(savePermission -> apiResponsePage.stream()
                                                                                  .anyMatch(permission -> permission.getId() != null
                                                                                          && permission.getId()
                                                                                                       .equals(savePermission.getId())))).isTrue();
        }
    }

    @Nested
    @DisplayName("when assign")
    class WhenAssign
    {
        @Test
        @DisplayName("POST: 'http://.../roles/{roleId}/permissions/{permissionId}' returns CREATED and the assigned permission ")
        void givenRoleAndPermission_whenAssign_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            Role savedRole = saveRole(roleTestFactory.createDefault());
            Permission savedPermission = savePermission(permissionTestFactory.createDefault());
            String uri = String.format("%s/{permissionId}", RolePermissionController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri, savedRole.getId(), savedPermission.getId()).contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
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
    }

    private Role saveRole(Role role) throws Exception
    {
        return roleService.create(role);
    }

    private Permission savePermission(Permission permission) throws Exception
    {
        return permissionService.create(permission);
    }

    List<Permission> saveRolePermissionFor(UUID roleId, List<UUID> permissionIds) throws Exception
    {
        List<Permission> result = new ArrayList<>(permissionIds.size());
        for (UUID permissionId : permissionIds)
        {
            result.add(rolePermissionService.assign(roleId, permissionId));
        }
        return result;
    }
}