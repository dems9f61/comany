package com.takeaway.authorization.oauth.entity;

import com.takeaway.authorization.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * User: StMinko Date: 06.11.2019 Time: 11:39
 *
 * <p>
 */
@Component
public class OAuthClientTestFactory extends AbstractTestFactory<OauthClient, OAuthClientTestFactory.Builder>
{

    @Override
    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<OauthClient>
    {
        private String clientId;

        private String resourceIds;

        private Boolean secretRequired = Boolean.FALSE;

        private String clientSecret;

        private Boolean scoped = Boolean.TRUE;

        private String scope;

        private String authorizedGrantTypes;

        private String registeredRedirectUri;

        private String authorities;

        private Integer accessTokenValidityInSeconds;

        private Integer refreshTokenValidityInSeconds;

        public Builder()
        {
            this.clientId = RandomStringUtils.randomAlphabetic(10);
            this.resourceIds =
                    RandomStringUtils.randomAlphabetic(3) + "," + RandomStringUtils.randomAlphabetic(3) + "," + RandomStringUtils.randomAlphabetic(5);
            this.secretRequired = true;
            this.clientSecret = RandomStringUtils.randomAlphabetic(10);
            this.scoped = true;
            this.scope = "read,write";
            this.authorizedGrantTypes = "password,refresh_token";
            this.registeredRedirectUri = URI.create(
                    RandomStringUtils.randomAlphabetic(4) + "." + RandomStringUtils.randomAlphabetic(4) + ".com//authorize")
                                            .toASCIIString();
            this.authorities = RandomStringUtils.randomAlphabetic(3) + "," + RandomStringUtils.randomAlphabetic(3);
            this.accessTokenValidityInSeconds = 84600 * 3;
            this.refreshTokenValidityInSeconds = 84600 * 7;
        }

        public Builder clientId(String clientId)
        {
            this.clientId = clientId;
            return this;
        }

        public Builder resourceIds(String resourceIds)
        {
            this.resourceIds = resourceIds;
            return this;
        }

        public Builder secretRequired(Boolean secretRequired)
        {
            this.secretRequired = secretRequired;
            return this;
        }

        public Builder clientSecret(String clientSecret)
        {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder scoped(Boolean scoped)
        {
            this.scoped = scoped;
            return this;
        }

        public Builder scope(String scope)
        {
            this.scope = scope;
            return this;
        }

        public Builder authorizedGrantTypes(String authorizedGrantTypes)
        {
            this.authorizedGrantTypes = authorizedGrantTypes;
            return this;
        }

        public Builder registeredRedirectUri(String registeredRedirectUri)
        {
            this.registeredRedirectUri = registeredRedirectUri;
            return this;
        }

        public Builder authorities(String authorities)
        {
            this.authorities = authorities;
            return this;
        }

        public Builder accessTokenValiditySeconds(Integer accessTokenValiditySeconds)
        {
            this.accessTokenValidityInSeconds = accessTokenValiditySeconds;
            return this;
        }

        public Builder refreshTokenValiditySeconds(Integer refreshTokenValiditySeconds)
        {
            this.refreshTokenValidityInSeconds = refreshTokenValiditySeconds;
            return this;
        }

        @Override
        public OauthClient create()
        {
            OauthClient oAuthClient = new OauthClient();
            return oAuthClient.setClientId(clientId)
                              .setClientResourceIds(resourceIds)
                              .setSecretRequired(secretRequired)
                              .setClientSecret(clientSecret)
                              .setClientScoped(scoped)
                              .setClientScope(scope)
                              .setClientRegisteredRedirectUri(registeredRedirectUri)
                              .setClientAuthorizedGrantTypes(authorizedGrantTypes)
                              .setClientAuthorities(authorities)
                              .setAccessTokenValidityInSeconds(accessTokenValidityInSeconds)
                              .setRefreshTokenValidityInSeconds(refreshTokenValidityInSeconds);
        }
    }
}
