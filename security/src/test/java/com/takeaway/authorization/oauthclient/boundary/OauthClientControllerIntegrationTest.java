package com.takeaway.authorization.oauthclient.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.takeaway.authorization.IntegrationTestSuite;
import com.takeaway.authorization.oauthclient.control.OauthClientService;
import com.takeaway.authorization.oauthclient.entity.OauthClient;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.ResponsePage;
import com.takeaway.authorization.runtime.security.boundary.AccessTokenParameter;
import com.takeaway.authorization.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko Date: 06.11.2019 Time: 11:36
 *
 * <p>
 */
@AutoConfigureMockMvc
class OauthClientControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OauthClientService oAuthClientService;

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../oauth-clients' returns UNAUTHORIZED for missing Authorization header  ")
        void givenMissingAuthorizationHeader_whenFindAll_thenStatus401() throws Exception
        {
            // Arrange
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET: 'http://.../oauth-clients' returns FORBIDDEN for missing scope  ")
        void givenMissingScope_whenFindAll_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .scopes("bad_scope")
                                                                            .build();
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../oauth-clients' returns FORBIDDEN for missing role  ")
        void givenMissingRole_whenFindAll_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                    .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET: 'http://.../oauth-clients' returns OK and an list of all oauth-clients ")
        void givenOAuthClients_whenFindAll_thenStatus200AndReturnFullList() throws Exception
        {
            // Arrange
            List<OauthClient> savedOauthClients = saveRandomOAuthClients(4);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                          .contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            ResponsePage<User> responsePage = objectMapper.readValue(contentAsString, new TypeReference<ResponsePage<User>>() {});
            assertThat(responsePage).isNotNull();
            assertThat(responsePage.getTotalElements()).isEqualTo(savedOauthClients.size());
            assertThat(savedOauthClients.stream()
                                        .allMatch(savedOAuthClient -> responsePage.stream()
                                                                                  .anyMatch(role -> role.getId() != null && role.getId()
                                                                                                                                .equals(savedOAuthClient.getId())))).isTrue();
        }

        @Test
        @DisplayName("GET: 'http://.../oauth-clients/{id}' returns NOT FOUND for unknown id ")
        void givenUnknownId_whenFindById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, unknownId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                               .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find [OauthClient] for Id [%s]", unknownId))));
        }

        @Test
        @DisplayName("GET: 'http://.../oauth-clients/{id}' returns OK and the requested oauth-client")
        void givenOAuthClient_whenFindById_thenStatus200AndReturnOAuthClient() throws Exception
        {
            // Arrange
            OauthClient persistedOauthClient = saveRandomOAuthClients(1).get(0);
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(get(uri, persistedOauthClient.getId()).header(HttpHeaders.AUTHORIZATION,
                                                                                                "Bearer " + obtainAccessToken())
                                                                                        .contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(status().isOk())
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            OauthClient oAuthClient = objectMapper.readValue(contentAsString, OauthClient.class);
            assertThat(oAuthClient).isNotNull();
            assertThat(oAuthClient.getId()).isEqualTo(persistedOauthClient.getId());
            assertThat(oAuthClient.getClientId()).isEqualTo(persistedOauthClient.getClientId());
            assertThat(oAuthClient.getClientResourceIds()).isEqualTo(persistedOauthClient.getClientResourceIds());
            assertThat(oAuthClient.getClientScoped()).isEqualTo(persistedOauthClient.getClientScoped());
            assertThat(oAuthClient.getClientScope()).isEqualTo(persistedOauthClient.getClientScope());
            assertThat(oAuthClient.getSecretRequired()).isEqualTo(persistedOauthClient.getSecretRequired());
            assertThat(oAuthClient.getClientAuthorizedGrantTypes()).isEqualTo(persistedOauthClient.getClientAuthorizedGrantTypes());
            assertThat(oAuthClient.getClientRegisteredRedirectUri()).isEqualTo(persistedOauthClient.getClientRegisteredRedirectUri());
            assertThat(oAuthClient.getClientAuthorities()).isEqualTo(persistedOauthClient.getClientAuthorities());
            assertThat(oAuthClient.getAccessTokenValidityInSeconds()).isEqualTo(persistedOauthClient.getAccessTokenValidityInSeconds());
            assertThat(oAuthClient.getRefreshTokenValidityInSeconds()).isEqualTo(persistedOauthClient.getRefreshTokenValidityInSeconds());
        }
    }

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenCreate_thenStatus401() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            OauthClient toPersist = oAuthClientTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns FORBIDDEN for missing role")
        void givenMissingRole_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            OauthClient toPersist = oAuthClientTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns CREATED if the creation request is valid")
        void givenValidCreateRequest_whenCreate_thenStatus201AndReturnNewOAuthClient() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.createDefault();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                           .contentType(APPLICATION_JSON_UTF8)
                                                           .content(requestAsJson))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andExpect(header().string(HttpHeaders.LOCATION, containsString(OauthClientController.BASE_URI)))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            OauthClient created = objectMapper.readValue(contentAsString, OauthClient.class);
            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getClientId()).isEqualTo(toPersist.getClientId());
            assertThat(created.getClientResourceIds()).isEqualTo(toPersist.getClientResourceIds());
            assertThat(created.getClientScoped()).isEqualTo(toPersist.getClientScoped());
            assertThat(created.getClientScope()).isEqualTo(toPersist.getClientScope());
            assertThat(created.getSecretRequired()).isEqualTo(toPersist.getSecretRequired());
            assertThat(created.getClientAuthorizedGrantTypes()).isEqualTo(toPersist.getClientAuthorizedGrantTypes());
            assertThat(created.getClientRegisteredRedirectUri()).isEqualTo(toPersist.getClientRegisteredRedirectUri());
            assertThat(created.getClientAuthorities()).isEqualTo(toPersist.getClientAuthorities());
            assertThat(created.getAccessTokenValidityInSeconds()).isEqualTo(toPersist.getAccessTokenValidityInSeconds());
            assertThat(created.getRefreshTokenValidityInSeconds()).isEqualTo(toPersist.getRefreshTokenValidityInSeconds());
            assertThat(created.getCreatedAt()).isNotNull();
            assertThat(created.getLastUpdatedAt()).isNotNull();
            assertThat(created.getCreatedBy()).isNotNull();
            assertThat(created.getLastUpdatedBy()).isNotNull();
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns CREATED with the minimal creation request")
        void givenMinimalValidCreateRequest_whenCreate_thenStatus201AndReturnNewOAuthClient() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .secretRequired(null)
                                                          .registeredRedirectUri(null)
                                                          .authorities(null)
                                                          .scoped(null)
                                                          .resourceIds(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                           .contentType(APPLICATION_JSON_UTF8)
                                                           .content(requestAsJson))
                                         .andExpect(status().isCreated())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andExpect(header().string(HttpHeaders.LOCATION, containsString(OauthClientController.BASE_URI)))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            OauthClient created = objectMapper.readValue(contentAsString, OauthClient.class);
            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getClientId()).isEqualTo(toPersist.getClientId());
            assertThat(created.getClientResourceIds()).isNull();
            assertThat(created.getClientScoped()).isTrue();
            assertThat(created.getClientScope()).isEqualTo(toPersist.getClientScope());
            assertThat(created.getSecretRequired()).isTrue();
            assertThat(created.getClientAuthorizedGrantTypes()).isEqualTo(toPersist.getClientAuthorizedGrantTypes());
            assertThat(created.getClientRegisteredRedirectUri()).isNull();
            assertThat(created.getClientAuthorities()).isNull();
            assertThat(created.getAccessTokenValidityInSeconds()).isEqualTo(toPersist.getAccessTokenValidityInSeconds());
            assertThat(created.getRefreshTokenValidityInSeconds()).isEqualTo(toPersist.getRefreshTokenValidityInSeconds());
            assertThat(created.getCreatedAt()).isNotNull();
            assertThat(created.getLastUpdatedAt()).isNotNull();
            assertThat(created.getCreatedBy()).isNotNull();
            assertThat(created.getLastUpdatedBy()).isNotNull();
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null client id")
        void givenNullOAuthClientId_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .clientId(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null client secret")
        void givenNullOAuthClientSecret_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .newClientSecret(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null client id")
        void givenNotMatchingNewSecretAndConfirmSecret_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .newClientSecret(RandomStringUtils.randomAlphabetic(5))
                                                          .confirmClientSecret(RandomStringUtils.randomAlphabetic(5))
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null access token validity")
        void givenNullOAuthAccessTokenValidity_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .accessTokenValiditySeconds(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null refresh token validity")
        void givenNullOAuthRefreshTokenValidity_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .refreshTokenValiditySeconds(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("POST: 'http://.../oauth-clients' returns BAD_REQUEST on null scope")
        void givenNullScope_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient toPersist = oAuthClientTestFactory.builder()
                                                          .scope(null)
                                                          .create();
            String requestAsJson = transformRequestToJSON(toPersist, DataView.POST.class);
            String uri = String.format("%s", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(post(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                     .contentType(APPLICATION_JSON_UTF8)
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
        @DisplayName("PUT: 'http://.../oauth-clients' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenDoFullUpdate_thenStatus401() throws Exception
        {
            // Arrange
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.createDefault();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("PUT: 'http://.../oauth-clients' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenDoFullUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            User update = userTestFactory.createDefault();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                                     .contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("PUT: 'http://.../users' returns FORBIDDEN for missing role")
        void givenMissingRole_whenDoFullUpdate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.createDefault();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act/ Assert
            mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                                     .contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("PUT: 'http://.../oauth-clients/{id}' returns OK on valid full request")
        void givenValidFullRequest_whenDoFullUpdate_thenStatus200AndReturnUpdated() throws Exception
        {
            // Arrange
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.createDefault();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            MvcResult mvcResult = mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                                           .contentType(APPLICATION_JSON_UTF8)
                                                                           .content(requestAsJson))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                                         .andExpect(jsonPath("$", notNullValue()))
                                         .andReturn();
            String contentAsString = mvcResult.getResponse()
                                              .getContentAsString();
            OauthClient updated = objectMapper.readValue(contentAsString, OauthClient.class);
            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(initial.getId());
            assertThat(updated.getClientId()).isEqualTo(update.getClientId());
            assertThat(updated.getClientResourceIds()).isEqualTo(update.getClientResourceIds());
            assertThat(updated.getClientScoped()).isEqualTo(update.getClientScoped());
            assertThat(updated.getClientScope()).isEqualTo(update.getClientScope());
            assertThat(updated.getSecretRequired()).isEqualTo(update.getSecretRequired());
            assertThat(updated.getClientAuthorizedGrantTypes()).isEqualTo(update.getClientAuthorizedGrantTypes());
            assertThat(updated.getClientRegisteredRedirectUri()).isEqualTo(update.getClientRegisteredRedirectUri());
            assertThat(updated.getClientAuthorities()).isEqualTo(update.getClientAuthorities());
            assertThat(updated.getAccessTokenValidityInSeconds()).isEqualTo(update.getAccessTokenValidityInSeconds());
            assertThat(updated.getRefreshTokenValidityInSeconds()).isEqualTo(update.getRefreshTokenValidityInSeconds());
        }

        @Test
        @DisplayName("PUT: 'http://.../oauth-clients/{id}' returns BAD REQUEST on null client id")
        void givenNullClientId_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.builder()
                                                       .clientId(null)
                                                       .create();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                     .contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PUT: 'http://.../oauth-clients/{id}' returns BAD REQUEST on null client secret")
        void givenNullClientSecret_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.builder()
                                                       .newClientSecret(null)
                                                       .create();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                     .contentType(APPLICATION_JSON_UTF8)
                                                     .content(requestAsJson))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        @DisplayName("PUT: 'http://.../oauth-clients/{id}' returns BAD REQUEST on null scope")
        void givenNullScope_whenDoFullUpdate_thenStatus400() throws Exception
        {
            // Arrange
            OauthClient initial = saveRandomOAuthClients(1).get(0);
            OauthClient update = oAuthClientTestFactory.builder()
                                                       .scope(null)
                                                       .create();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);
            String requestAsJson = transformRequestToJSON(update, DataView.PUT.class);

            // Act / Assert
            mockMvc.perform(put(uri, initial.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
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
        @DisplayName("DELETE: 'http://.../oauth-clients' returns UNAUTHORIZED for missing Authorization header")
        void givenMissingAuthorizationHeader_whenDelete_thenStatus401() throws Exception
        {
            // Arrange
            OauthClient toDelete = saveRandomOAuthClients(1).get(0);
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, toDelete.getId()).contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE: 'http://.../oauth-clients' returns FORBIDDEN for missing scope")
        void givenMissingScope_whenDelete_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .clientId("clientWithBadScope")
                                                                            .clientSecret("secret")
                                                                            .build();
            OauthClient toDelete = saveRandomOAuthClients(1).get(0);
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                                         .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../oauth-clients' returns FORBIDDEN for missing role")
        void givenMissingRole_whenCreate_thenStatus403() throws Exception
        {
            // Arrange
            AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                            .userName("userWithNoRole")
                                                                            .userPassword("user")
                                                                            .build();
            OauthClient toDelete = saveRandomOAuthClients(1).get(0);
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken(accessTokenParameter))
                                                         .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("DELETE: 'http://.../oauth-clients/{id}' returns NOT FOUND if the specified id doesn't exist")
        void givenUnknownId_whenDeleteById_thenStatus404() throws Exception
        {
            // Arrange
            UUID unknownId = UUID.randomUUID();
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act/ Assert
            mockMvc.perform(delete(uri, unknownId).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                  .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$", containsString(String.format("Could not find [OauthClient] for Id [%s]!", unknownId))));
        }

        @Test
        @DisplayName("DELETE: 'http://.../oauth-clients/{id}' returns NO CONTENT if the specified id exists")
        void givenRole_whenDeleteById_thenStatus204() throws Exception
        {
            // Arrange
            OauthClient toDelete = saveRandomOAuthClients(1).get(0);
            String uri = String.format("%s/{id}", OauthClientController.BASE_URI);

            // Act / Assert
            mockMvc.perform(delete(uri, toDelete.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + obtainAccessToken())
                                                         .contentType(APPLICATION_JSON_UTF8))
                   .andExpect(status().isNoContent());
        }
    }

    private List<OauthClient> saveRandomOAuthClients(int count)
    {
        int normalizedCount = count <= 0 ? 1 : count;
        List<OauthClient> result = new ArrayList<>(count);
        for (int i = 0; i < normalizedCount; i++)
        {
            result.add(oAuthClientService.create(oAuthClientTestFactory.createDefault()));
        }

        return result;
    }
}
