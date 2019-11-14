package com.takeaway.authorization.oauth.control;

import com.takeaway.authorization.IntegrationTestSuite;
import com.takeaway.authorization.oauth.boundary.AccessTokenParameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko Date: 10.11.2019 Time: 12:48
 * <p>
 */
class OauthAuthorizationServerIntegrationTest extends IntegrationTestSuite
{
    @Test
    @DisplayName("Accessing token returns BAD CREDENTIALS for bad user credentials")
    void givenBadUserCredentials_whenAccessToken_thenReturnsBadCredentials() throws Exception
    {
        // Arrange
        AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                        .userPassword(RandomStringUtils.randomAlphabetic(8))
                                                                        .userName(RandomStringUtils.randomAlphabetic(9))
                                                                        .build();
        // Act
        String response = obtainAccessToken(accessTokenParameter);

        // Assert
        assertThat(response).isNotNull()
                            .isEqualTo("Bad credentials");
    }

    @Test
    @DisplayName("Accessing token returns empty string for bad client credentials")
    void givenBadClientCredentials_whenAccessToken_thenReturnsEmptyString() throws Exception
    {
        // Arrange
        AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                        .clientId(RandomStringUtils.randomAlphabetic(8))
                                                                        .clientSecret(RandomStringUtils.randomAlphabetic(9))
                                                                        .build();
        // Act
        String response = obtainAccessToken(accessTokenParameter);

        // Assert
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Accessing token returns access token on valid request")
    void givenValidRequest_whenAccessToken_thenSucceed() throws Exception
    {
        // Arrange
        AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                        .build();
        // Act
        String response = obtainAccessToken(accessTokenParameter);

        // Assert
        assertThat(response).isNotNull()
                            .isNotEqualTo("Bad credentials");
    }
}
