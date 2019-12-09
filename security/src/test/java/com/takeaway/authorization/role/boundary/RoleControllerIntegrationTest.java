package com.takeaway.authorization.role.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authorization.IntegrationTestSuite;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.ResponsePage;
import com.takeaway.authorization.runtime.security.boundary.AccessTokenParameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
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
 * User: StMinko Date: 14.10.2019 Time: 16:42
 *
 * <p>
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
        @DisplayName("GET: 'http://.../roles' returns UNAUTHORIZED for missing Authorization header  ")
        void givenMissingAuthorizationHeader_whenFindAll_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET: 'http://.../roles' returns FORBIDDEN for missing scope  ")
        void givenMissingScope_whenFindAll_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").scopes("bad_scope").build();
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter)).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../roles' returns FORBIDDEN for missing role  ")
        void givenMissingRole_whenFindAll_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter)).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../roles' returns OK and an list of all roles ")
        void givenRoles_whenFindAll_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            List<Role> savedRoles = saveRandomRoles(4);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri).contentType(APPLICATION_JSON_UTF8)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            ResponsePage<Role> responsePage = objectMapper.readValue(contentAsString, new TypeReference<ResponsePage<Role>>() {});
            assertThat(responsePage).isNotNull();
            assertThat(responsePage.getTotalElements()).isEqualTo(savedRoles.size());
            assertThat(savedRoles.stream()
                                        .allMatch(savedRole -> responsePage.stream()
                                                        .anyMatch(role -> role.getId() != null && role.getId().equals(savedRole.getId()))))
                    .isTrue();
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}' returns NOT FOUND for unknown id ")
        void givenUnknownId_whenFindById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, unknownId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken()).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", notNullValue()))
                    .andExpect(jsonPath("$", containsString(String.format("Could not find [Role] for Id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}' returns OK and the requested permission")
        void givenRole_whenFindById_thenStatus200AndReturnRole() throws Exception
        {
            // Arrange
            Role persistedRole = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri, persistedRole.getId())
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
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
        @DisplayName("POST: 'http://.../roles' returns UNAUTHORIZED for missing Authorization header  ")
        void givenMissingAuthorizationHeader_whenFindAll_thenStatus401() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").build();
            Role toPersist = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns FORBIDDEN for missing role")
        void givenMissingRole_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            Role toPersist = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns CREATED if the creation request is valid")
        void givenValidCreateRequest_whenCreateRole_thenStatus201AndReturnNewRole() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andExpect(status().isCreated())
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andExpect(header().string(HttpHeaders.LOCATION, containsString(RoleController.BASE_URI)))
                            .andReturn();

            String contentAsString = mvcResult.getResponse().getContentAsString();
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
            Role toPersist = roleTestFactory.builder().description(null).create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);

            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andExpect(status().isCreated())
                            .andExpect(header().string(HttpHeaders.LOCATION, containsString(RoleController.BASE_URI)))
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();

            String contentAsString = mvcResult.getResponse().getContentAsString();
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
            Role toPersist = roleTestFactory.builder().name("").create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken()).contentType(APPLICATION_JSON_UTF8).content(requestAsJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../roles' returns BAD_REQUEST on blank description")
        void givenBlankDescription_whenCreateRole_thenStatus400() throws Exception
        {
            // Arrange
            Role toPersist = roleTestFactory.builder().description("").create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken()).contentType(APPLICATION_JSON_UTF8).content(requestAsJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", notNullValue()));
        }
    }

    @Nested
    @DisplayName("when update")
    class WhenUpdate
    {
        @Test
        @DisplayName("PUT: 'http://.../roles' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenDoFullUpdate_thenStatus401() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.createDefault();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("PUT: 'http://.../roles' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenDoFullUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").build();
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.createDefault();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("PUT: 'http://.../roles' returns FORBIDDEN for missing role")
        void givenMissingRole_whenDoFullUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.createDefault();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act/ Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

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
            MvcResult mvcResult = mockMvc.perform(put(uri, initial.getId())
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
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
            Role update = roleTestFactory.builder().name(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
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
            Role update = roleTestFactory.builder().name("   ").create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
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
            Role update = roleTestFactory.builder().description(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenDoPartialUpdate_thenStatus401() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder().name(RandomStringUtils.randomAlphabetic(8)).description(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PATCH.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8).content(requestAsJson)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenDoPartialUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").build();
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder().name(RandomStringUtils.randomAlphabetic(8)).description(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PATCH.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles' returns FORBIDDEN for missing role")
        void givenMissingRole_whenDoPartialUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder().name(RandomStringUtils.randomAlphabetic(8)).description(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PATCH.class);

            // Act/ Assert
            mockMvc.perform(patch(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8)
                                    .content(requestAsJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("PATCH: 'http://.../roles/{id}' returns OK on valid request with only name")
        void givenValidRequestWithOnlyName_whenDoPartialUpdate_thenStatus200AndReturnUpdated() throws Exception
        {
            // Arrange
            Role initial = saveRandomRoles(1).get(0);
            Role update = roleTestFactory.builder().name("PERMISSION_MANAGER").description(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(patch(uri, initial.getId())
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
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
            Role update = roleTestFactory.builder().name(null).create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(patch(uri, initial.getId())
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
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
            Role update = roleTestFactory.builder().description(" ").create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
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
            Role update = roleTestFactory.builder().name(" ").create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
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
            Role update = roleTestFactory.builder().description(" ").create();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(patch(uri, initial.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                    .contentType(APPLICATION_JSON_UTF8)
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
        @DisplayName("DELETE: 'http://.../roles' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenDelete_thenStatus401() throws Exception
        {
            // Arrange
            Role toDelete = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, toDelete.getId()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenDelete_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").build();
            Role toDelete = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles' returns FORBIDDEN for missing role")
        void givenMissingRole_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            Role toDelete = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{id}' returns NOT FOUND if the specified id doesn't exist")
        void givenUnknownUuid_whenDeleteById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, unknownId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken()).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", notNullValue()))
                    .andExpect(jsonPath("$", containsString(String.format("Could not find [Role] for Id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../roles/{id}' returns NO CONTENT if the specified id exists")
        void givenRole_whenDeleteById_thenStatus204() throws Exception
        {
            // Arrange
            Role toDelete = saveRandomRoles(1).get(0);
            String uri = String.format("%s/{id}", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken()).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("when revise")
    class WhenRevise
    {
        @Test
        @DisplayName("GET: 'http://.../roles/{id}/auditTrails' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenFindAuditTrails_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s/{id}/auditTrails", RoleController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(get(uri, UUID.randomUUID()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}/auditTrails' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenFindAuditTrails_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().clientId("clientWithBadScope").clientSecret("secret").build();
            String uri = String.format("%s/{id}/auditTrails", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, UUID.randomUUID())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}/auditTrails' returns FORBIDDEN for missing role")
        void givenMissingRole_whenFindAuditTrails_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder().userName("userWithNoRole").userPassword("user").build();
            String uri = String.format("%s/{id}/auditTrails", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, UUID.randomUUID())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}/revisions' returns OK and Revisions")
        void givenIdWithHistory_whenFindRevisions_thenStatus200() throws Exception
        {
            // Arrange
            Role initial = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(initial, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);
            String accessToken = obtainAccessToken();

            // 1-Action: CREATE
            MvcResult mvcCreationResult = mockMvc.perform(post(uri)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andReturn();
            String contentAsString = mvcCreationResult.getResponse().getContentAsString();
            Role created = objectMapper.readValue(contentAsString, Role.class);
            Role update = roleTestFactory.createDefault();
            uri = String.format("%s/{id}", RoleController.BASE_URI);
            requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // 2-Action: MODIFY
            mockMvc.perform(put(uri, created.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(APPLICATION_JSON_UTF8).content(requestAsJson));

            // 3-Action: DELETE
            mockMvc.perform(delete(uri, created.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(APPLICATION_JSON_UTF8));

            uri = String.format("%s/{id}/revisions", RoleController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, created.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", notNullValue()))
                    .andExpect(jsonPath("$.numberOfElements", is(3)))
                    .andExpect(jsonPath("$.totalElements", is(3)));
        }

        @Test
        @DisplayName("GET: 'http://.../roles/{id}/auditTrails' returns OK and audit trails")
        void givenIdWithHistory_whenFindAuditTrails_thenStatus200() throws Exception
        {
            // Arrange
            Role initial = roleTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(initial, DataView.POST.class);
            String uri = String.format("%s", RoleController.BASE_URI);
            String accessToken = obtainAccessToken();

            // 1-Action: CREATE

            MvcResult mvcCreationResult = mockMvc.perform(post(uri)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andReturn();
            String createdContentAsString = mvcCreationResult.getResponse().getContentAsString();
            Role created = objectMapper.readValue(createdContentAsString, Role.class);
            Role update = roleTestFactory.createDefault();
            uri = String.format("%s/{id}", RoleController.BASE_URI);
            requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // 2-Action: MODIFY
            MvcResult mvcResult = mockMvc.perform(put(uri, created.getId())
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                            .contentType(APPLICATION_JSON_UTF8)
                                            .content(requestAsJson))
                            .andReturn();
            String updatedContentAsString = mvcResult.getResponse().getContentAsString();
            Role updated = objectMapper.readValue(updatedContentAsString, Role.class);

            // 3-Action: DELETE
            mockMvc.perform(delete(uri, created.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(APPLICATION_JSON_UTF8));

            uri = String.format("%s/{id}/auditTrails", RoleController.BASE_URI);

            // Act / Assert
            MvcResult revisionResult = mockMvc.perform(get(uri, created.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(APPLICATION_JSON_UTF8))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", notNullValue()))
                            .andReturn();
            String revisionResultAsString = revisionResult.getResponse().getContentAsString();
            ResponsePage<AuditTrail<UUID, Role>> responsePage = objectMapper.readValue(revisionResultAsString, new TypeReference<ResponsePage<AuditTrail<UUID, Role>>>() {});
            assertThat(responsePage).isNotNull().hasSize(3);

            responsePage.forEach(page -> {
                        RevisionType revisionType = page.getRevisionType();
                        Role entity = page.getEntity();
                        switch (revisionType)
                        {
                            case ADD:
                                {
                                    assertThat(entity.getId()).isEqualTo(created.getId());
                                    assertThat(entity.getName()).isEqualTo(created.getName());
                                    assertThat(entity.getDescription()).isEqualTo(created.getDescription());
                                    assertThat(entity.getLastUpdatedAt()).isEqualTo(created.getLastUpdatedAt());
                                    assertThat(entity.getLastUpdatedBy()).isEqualTo(created.getLastUpdatedBy());
                                    assertThat(entity.getCreatedAt()).isNull(); // NOT AUDITED
                                    assertThat(entity.getCreatedBy()).isNull(); // NOT AUDITED
                                    assertThat(entity.getVersion()).isEqualTo(created.getVersion());
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
                                    assertThat(entity.getCreatedAt()).isNull(); // NOT AUDITED
                                    assertThat(entity.getCreatedBy()).isNull(); // NOT AUDITED
                                    assertThat(entity.getVersion()).isEqualTo(updated.getVersion());
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

    private List<Role> saveRandomRoles(int count) throws Exception
    {
        int normalizedCount = count <= 0 ? 1 : count;
        List<Role> result = new ArrayList<>(count);
        for (int i = 0; i < normalizedCount; i++)
        {
            result.add(roleService.create(roleTestFactory.createDefault()));
        }

        return result;
    }
}
