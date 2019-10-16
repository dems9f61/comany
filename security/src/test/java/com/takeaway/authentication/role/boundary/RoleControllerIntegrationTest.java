package com.takeaway.authentication.role.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authentication.IntegrationTestSuite;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.boundary.DataView;
import com.takeaway.authentication.role.control.RoleService;
import com.takeaway.authentication.role.entity.Role;
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
 * User: StMinko
 * Date: 14.10.2019
 * Time: 16:42
 * <p/>
 */
@AutoConfigureMockMvc
class RoleControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../roles' returns OK and an list of all roles ")
        void givenSourceSystems_whenFindAll_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            List<Role> expectedRoles = saveRandomRoles(4);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            ApiResponsePage<Role> apiResponsePage = objectMapper.readValue(contentAsString, new TypeReference<ApiResponsePage<Role>>() {});
            assertThat(apiResponsePage).isNotNull();
            assertThat(apiResponsePage.getTotalElements()).isEqualTo(expectedRoles.size());
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}' returns BAD REQUEST for unknown id ")
        void givenUnknownId_whenFindById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, unknownId).contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}' returns OK and the requested source system")
        void givenRole_whenFindById_thenStatus200AndReturnRole() throws Exception
        {
            // Arrange
            Role persistedRole = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri, persistedRole.getId()).contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role role = objectMapper.readValue(contentAsString, Role.class);
            assertThat(role).isNotNull();
            assertThat(role.getId()).isEqualTo(persistedRole.getId());
            assertThat(role.getName()).isEqualTo(persistedRole.getName());
            assertThat(role.getDescription()).isEqualTo(persistedRole.getDescription());
        }
    }

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../roles' returns CREATED if the creation request is valid")
        void givenValidCreateRequest_whenCreateRole_thenStatus201AndReturnNewRole() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                                           .content(requestAsJson))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andExpect(header().string(HttpHeaders.LOCATION, containsString(RoleController.BASE_URI)))
                                         .andReturn();

            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role created = objectMapper.readValue(contentAsString, Role.class);
            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getName()).isEqualTo(toPersist.getName());
            assertThat(created.getDescription()).isEqualTo(toPersist.getDescription());
            assertThat(created.getCreatedAt()).isNotNull();
            assertThat(created.getLastUpdatedAt()).isNotNull();
            assertThat(created.getCreatedBy()).isNotNull();
            assertThat(created.getLastUpdatedBy()).isNotNull();
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns CREATED if the creation request without description")
        void givenCreateRequestWithoutDesc_whenCreateRole_thenStatus201AndReturnNewRole() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.builder()
                                            .description(null)
                                            .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);

            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                                           .content(requestAsJson))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andExpect(header().string(HttpHeaders.LOCATION, containsString(RoleController.BASE_URI)))
                                         .andReturn();

            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role created = objectMapper.readValue(contentAsString, Role.class);
            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getName()).isEqualTo(toPersist.getName());
            assertThat(created.getDescription()).isNull();
            assertThat(created.getCreatedAt()).isNotNull();
            assertThat(created.getLastUpdatedAt()).isNotNull();
            assertThat(created.getCreatedBy()).isNotNull();
            assertThat(created.getLastUpdatedBy()).isNotNull();
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns BAD_REQUEST on blank identifier")
        void givenBlankName_whenCreateRole_thenStatus400() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.builder()
                                            .name("")
                                            .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }
    }

    @Nested
    @DisplayName("when update")
    class WhenUpdate
    {
        @Test
        @DisplayName("PUT: 'http://.../roles/{id}' returns OK on valid full request")
        void givenValidFullRequest_whenDoFullUpdate_thenStatus200AndReturnUpdated() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.createDefault();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
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
            Role updated = objectMapper.readValue(contentAsString, Role.class);
            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(initial.getId());
            assertThat(updated.getName()).isEqualTo(update.getName());
            assertThat(updated.getDescription()).isEqualTo(update.getDescription());
        }

        @Test
        @DisplayName("PUT: 'http://.../roles/{id}' returns BAD REQUEST on null name")
        void givenNullName_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .name(null)
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PUT: 'http://.../roles/{id}' returns BAD REQUEST on empty name")
        void givenBlankName_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .name("   ")
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PUT: 'http://.../roles/{id}' returns BAD REQUEST on null description request")
        void givenNullDescription_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .description(null)
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns OK on valid request with only name")
        void givenValidRequestWithOnlyName_whenDoPartialUpdate_thenStatus200AndReturnUpdated() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .description(null)
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(patch(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                                             .content(requestAsJson))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role updated = objectMapper.readValue(contentAsString, Role.class);
            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(initial.getId());
            assertThat(updated.getName()).isEqualTo(update.getName());
            assertThat(updated.getDescription()).isEqualTo(initial.getDescription());
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns OK on valid request with only description")
        void givenValidRequestWithOnlyDesc_whenDoPartialUpdate_thenStatus200AndReturnUpdated() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .name(null)
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(patch(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                                             .content(requestAsJson))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            Role updated = objectMapper.readValue(contentAsString, Role.class);
            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(initial.getId());
            assertThat(updated.getName()).isEqualTo(initial.getName());
            assertThat(updated.getDescription()).isEqualTo(update.getDescription());
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns BAD REQUEST on empty description request")
        void givenEmptyDescription_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .description(" ")
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns BAD REQUEST on empty name request")
        void givenEmptyName_whenDoPartialUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .name(" ")
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                       .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns BAD REQUEST on empty description request")
        void givenEmptyDescription_whenDoPartialUpdate_thenStatus400() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder()
                                         .description(" ")
                                         .create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
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
        @DisplayName("DELETE: 'http://.../roles/{id}' returns NOT FOUND if the specified id doesn't exist")
        void givenUnknownUuid_whenDeleteById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, unknownId).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find an entity by the specified id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{id}' returns NO CONTENT if the specified id exists")
        void givenRole_whenDeleteById_thenStatus204() throws Exception
        {
            // Arrange
            Role toDelete = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNoContent());
        }
    }

    private List<Role> saveRandomRoles(int count) throws Exception
    {
        int normalizedCount = count <= 0 ? 1 : count;
        List<Role> result = new ArrayList<>(count);
        for (int i = 0; i < normalizedCount; i++)
        {
            Role aDefault = roleTestFactory.createDefault();
            result.add(roleService.create(aDefault));
        }

        return result;
    }
}