package com.takeaway.authentication.permission.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authentication.IntegrationTestSuite;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.integrationsupport.entity.AuditTrail;
import com.takeaway.authentication.permission.control.PermissionService;
import com.takeaway.authentication.permission.entity.Permission;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.envers.RevisionType;
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
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 12:08
 * <p/>
 */
@AutoConfigureMockMvc
class PermissionControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../permissions' returns OK and an list of all permissions ")
        void givenPermissions_whenFindAll_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            List<Permission> savedPermissions = saveRandomPermissions(RandomUtils.nextInt(1, 4));
            String uri = String.format("%s", PermissionController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            ApiResponsePage<Permission> apiResponsePage = objectMapper.readValue(contentAsString,
                                                                                 new TypeReference<ApiResponsePage<Permission>>() {});

            assertThat(apiResponsePage).isNotNull();
            assertThat(apiResponsePage.getTotalElements()).isEqualTo(savedPermissions.size());
            assertThat(savedPermissions.stream()
                                       .allMatch(savedPermission -> apiResponsePage.stream()
                                                                                   .anyMatch(permission -> permission.getId() != null
                                                                                           && permission.getId()
                                                                                                        .equals(savedPermission.getId())))).isTrue();
        }

        @Test
        @DisplayName("GET: 'http://.../permissions/{id}' returns BAD REQUEST for unknown id ")
        void givenUnknownId_whenFindById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, unknownId).contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("GET: 'http://.../permissions/{id}' returns OK and the requested permission")
        void givenPermission_whenFindById_thenStatus200AndReturnPermission() throws Exception
        {
            // Arrange
            Permission savedPermission = saveRandomPermissions(1).get(0);
            UUID id = savedPermission.getId();
            String uri = String.format("%s/{id}", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$.id", is(id.toString())))
                   .andExpect(jsonPath("$.name", is(savedPermission.getName())))
                   .andExpect(jsonPath("$.description", is(savedPermission.getDescription())))
                   .andExpect(jsonPath("$.createdAt", notNullValue()))
                   .andExpect(jsonPath("$.createdBy", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedAt", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedBy", notNullValue()));
        }
    }

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../permissions' returns CREATED if the creation request is valid")
        void givenValidCreateRequest_whenCreatePermission_thenStatus201AndReturnNewPermission() throws Exception
        {
            // Arrange
            Permission toPersist = permissionTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isCreated())
                   .andExpect(header().string(HttpHeaders.LOCATION, notNullValue()))
                   .andExpect(header().string(HttpHeaders.LOCATION, containsString(PermissionController.BASE_URI)))
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$.id", notNullValue()))
                   .andExpect(jsonPath("$.name", is(toPersist.getName())))
                   .andExpect(jsonPath("$.description", is(toPersist.getDescription())))
                   .andExpect(jsonPath("$.createdAt", notNullValue()))
                   .andExpect(jsonPath("$.createdBy", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedAt", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedBy", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../permissions' returns CREATED if the creation request without description")
        void givenCreateRequestWithoutDesc_whenCreatePermission_thenStatus201AndReturnNewPermission() throws Exception
        {
            // Arrange
            Permission toPersist = permissionTestFactory.builder()
                                                        .description(null)
                                                        .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);

            String uri = String.format("%s", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isCreated())
                   .andExpect(header().string(HttpHeaders.LOCATION, containsString(PermissionController.BASE_URI)))
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$.id", notNullValue()))
                   .andExpect(jsonPath("$.name", is(toPersist.getName())))
                   .andExpect(jsonPath("$.description", nullValue()))
                   .andExpect(jsonPath("$.createdAt", notNullValue()))
                   .andExpect(jsonPath("$.createdBy", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedAt", notNullValue()))
                   .andExpect(jsonPath("$.lastUpdatedBy", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../permissions' returns BAD_REQUEST on blank identifier")
        void givenBlankName_whenCreatePermission_thenStatus400() throws Exception
        {
            // Arrange
            Permission toPersist = permissionTestFactory.builder()
                                                        .name("")
                                                        .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../permissions' returns BAD_REQUEST on blank description")
        void givenBlankDescription_whenCreatePermission_thenStatus400() throws Exception
        {
            // Arrange
            Permission toPersist = permissionTestFactory.builder()
                                                        .description("")
                                                        .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", PermissionController.BASE_URI);

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
        @DisplayName("DELETE: 'http://.../permissions/{id}' returns NOT FOUND if the specified id doesn't exist")
        void givenUnknownUuid_whenDeleteById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", PermissionController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, unknownId).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../permissions/{id}' returns NO CONTENT if the specified id exists")
        void givenPermission_whenDeleteById_thenStatus204() throws Exception
        {
            // Arrange
            Permission toDelete = saveRandomPermissions(1).get(0);
            String uri = String.format("%s/{id}", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("when revise")
    class WhenRevise
    {
        @Test
        @DisplayName("GET: 'http://.../permissions/{id}/changes' returns OK and Revisions")
        void givenIdWithHistory_whenFindHistory_thenStatus200() throws Exception
        {
            // Arrange
            Permission initial = permissionTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(initial, DataView.POST.class);
            String uri = String.format("%s", PermissionController.BASE_URI);

            // 1-Action: CREATE
            MvcResult mvcCreationResult = mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                                                   .content(requestAsJson))
                                                 .andReturn();
            String createdContentAsString = mvcCreationResult.getResponse()
                                                             .getContentAsString();
            Permission created = objectMapper.readValue(createdContentAsString, Permission.class);
            Permission update = permissionTestFactory.createDefault();
            uri = String.format("%s/{id}", PermissionController.BASE_URI);
            requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // 2-Action: MODIFY
            mockMvc.perform(put(uri, created.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson));

            // 3-Action: DELETE
            mockMvc.perform(delete(uri, created.getId()).contentType(APPLICATION_JSON_UTF8));

            uri = String.format("%s/{id}/changes", PermissionController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, created.getId()).contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$.numberOfElements", is(3)))
                   .andExpect(jsonPath("$.totalElements", is(3)));
        }

        @Test
        @DisplayName("GET: 'http://.../permissions/{id}/revisions' returns OK and Revisions")
        void givenIdWithHistory_whenFindRevisions_thenStatus200AndRet() throws Exception
        {
            // Arrange
            Permission initial = permissionTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(initial, DataView.POST.class);
            String uri = String.format("%s", PermissionController.BASE_URI);

            // 1-Action: CREATE
            MvcResult mvcCreationResult = mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                                                   .content(requestAsJson))
                                                 .andReturn();
            String createdContentAsString = mvcCreationResult.getResponse()
                                                             .getContentAsString();
            Permission created = objectMapper.readValue(createdContentAsString, Permission.class);
            Permission update = permissionTestFactory.createDefault();
            uri = String.format("%s/{id}", PermissionController.BASE_URI);
            requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // 2-Action: MODIFY
            MvcResult mvcResult = mockMvc.perform(put(uri, created.getId()).contentType(APPLICATION_JSON_UTF8)
                                                                           .content(requestAsJson))
                                         .andReturn();
            String updatedContentAsString = mvcResult.getResponse()
                                                     .getContentAsString();
            Permission updated = objectMapper.readValue(updatedContentAsString, Permission.class);

            // 3-Action: DELETE
            mockMvc.perform(delete(uri, created.getId()).contentType(APPLICATION_JSON_UTF8));

            uri = String.format("%s/{id}/auditTrails", PermissionController.BASE_URI);

            // Act / Assert
            MvcResult revisionResult = mockMvc.perform(get(uri, created.getId()).contentType(MediaType.APPLICATION_JSON_UTF8))
                                              .andExpect(status().isOk())
                                              .andExpect(jsonPath("$", notNullValue()))
                                              .andReturn();
            String revisionResultAsString = revisionResult.getResponse()
                                                          .getContentAsString();
            ApiResponsePage<AuditTrail<UUID, Permission>> apiResponsePage = objectMapper.readValue(revisionResultAsString,
                                                                                 new TypeReference<ApiResponsePage<AuditTrail<UUID, Permission>>>() {});
            assertThat(apiResponsePage).isNotNull()
                                       .hasSize(3);

            apiResponsePage.forEach(page -> {
                RevisionType revisionType = page.getRevisionType();
                Permission entity = page.getEntity();
                switch (revisionType)
                {
                    case ADD:
                    {
                        assertThat(entity.getId()).isEqualTo(created.getId());
                        assertThat(entity.getName()).isEqualTo(created.getName());
                        assertThat(entity.getDescription()).isEqualTo(created.getDescription());
                        assertThat(entity.getLastUpdatedAt()).isEqualTo(created.getLastUpdatedAt());
                        assertThat(entity.getLastUpdatedBy()).isEqualTo(created.getLastUpdatedBy());
                        assertThat(entity.getCreatedAt()).isNull(); //NOT AUDITED
                        assertThat(entity.getCreatedBy()).isNull();  //NOT AUDITED
                        break;
                    }
                    case MOD:
                    case DEL:
                    {
                        assertThat(entity.getId()).isEqualTo(updated.getId());
                        assertThat(entity.getName()).isEqualTo(updated.getName());
                        assertThat(entity.getDescription()).isEqualTo(updated.getDescription());
                        assertThat(entity.getLastUpdatedAt()).isEqualTo(updated.getLastUpdatedAt());
                        assertThat(entity.getLastUpdatedBy()).isEqualTo(updated.getLastUpdatedBy());
                        assertThat(entity.getCreatedAt()).isNull(); //NOT AUDITED
                        assertThat(entity.getCreatedBy()).isNull();  //NOT AUDITED
                        break;
                    }
                    default:
                    {
                        fail(String.format("Should not happen: Unexpected revision type [%s]!", revisionType));
                    }
                }
            });
        }
    }

    private List<Permission> saveRandomPermissions(int count) throws Exception
    {
        int normalizedCount = count <= 0 ? 1 : count;
        List<Permission> result = new ArrayList<>(count);
        for (int i = 0; i < normalizedCount; i++)
        {
            result.add(permissionService.create(permissionTestFactory.createDefault()));
        }

        return result;
    }
}