package com.takeaway.authorization.runtime.security.boundary;

import lombok.Data;

/**
 * User: StMinko Date: 10.11.2019 Time: 12:53
 *
 * <p>
 */
@Data
public final class AccessTokenParameter
{
    private final String grantType;

    private final String clientId;

    private final String clientSecret;

    private final String userName;

    private final String userPassword;

    private final String scopes;

    private AccessTokenParameter(String grantType, String clientId, String clientSecret, String userName, String userPassword, String scopes)
    {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userName = userName;
        this.userPassword = userPassword;
        this.scopes = scopes;
    }

    public static AccessTokenParameterBuilder builder()
    {
        return new AccessTokenParameterBuilder();
    }

    public static class AccessTokenParameterBuilder
    {
        private String grantType;

        private String clientId;

        private String clientSecret;

        private String userName;

        private String userPassword;

        private String scopes;

        AccessTokenParameterBuilder()
        {
            this.grantType = "password";
            this.clientId = "client";
            this.clientSecret = "secret";
            this.userName = "admin";
            this.userPassword = "admin";
        }

        public AccessTokenParameterBuilder grantType(String grantType)
        {
            this.grantType = grantType;
            return this;
        }

        public AccessTokenParameterBuilder clientId(String clientId)
        {
            this.clientId = clientId;
            return this;
        }

        public AccessTokenParameterBuilder clientSecret(String clientSecret)
        {
            this.clientSecret = clientSecret;
            return this;
        }

        public AccessTokenParameterBuilder userName(String userName)
        {
            this.userName = userName;
            return this;
        }

        public AccessTokenParameterBuilder userPassword(String userPassword)
        {
            this.userPassword = userPassword;
            return this;
        }

        public AccessTokenParameterBuilder scopes(String scopes)
        {
            this.scopes = scopes;
            return this;
        }

        public AccessTokenParameter build()
        {
            return new AccessTokenParameter(grantType, clientId, clientSecret, userName, userPassword, scopes);
        }

        public String toString()
        {
            return "AccessTokenParameter.AccessTokenParameterBuilder(grantType="
                + this.grantType
                + ", clientId="
                + this.clientId
                + ", clientSecret="
                + this.clientSecret
                + ", userName="
                + this.userName
                + ", userPassword="
                + this.userPassword
                + ", scopes="
                + this.scopes
                + ")";
        }
    }
}
