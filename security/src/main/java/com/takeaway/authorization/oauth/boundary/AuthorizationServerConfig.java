package com.takeaway.authorization.oauth.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * User: StMinko Date: 06.11.2019 Time: 16:42
 *
 * <p>
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{
    // =========================== Class Variables ===========================

    static String REALM = "TAKEAWAY_REALM";

    // =============================  Variables  =============================

    private final TokenStore tokenStore;

    private final UserApprovalHandler userApprovalHandler;

    private final AuthenticationManager authenticationManager;

    private final ClientDetailsService clientDetailsService;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore,
                                     UserApprovalHandler userApprovalHandler,
                                     @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
                                     ClientDetailsService clientDetailsService)
    {
        this.tokenStore = tokenStore;
        this.userApprovalHandler = userApprovalHandler;
        this.authenticationManager = authenticationManager;
        this.clientDetailsService = clientDetailsService;
    }

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        super.configure(endpoints);
        endpoints.tokenStore(tokenStore)
                 .userApprovalHandler(userApprovalHandler)
                 .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
    {
        oauthServer.realm(REALM);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
