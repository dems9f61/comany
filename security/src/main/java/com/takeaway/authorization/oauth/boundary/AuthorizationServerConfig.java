package com.takeaway.authorization.oauth.boundary;

import com.takeaway.authorization.oauth.control.SecurityInfoTokenEnhancer;
import com.takeaway.authorization.oauth.entity.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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

  private final UserDetailsService userDetailsService;

  private final JwtAccessTokenConverter accessTokenConverter;

  private final SecurityInfoTokenEnhancer securityInfoTokenEnhancer;

  @Autowired
  public AuthorizationServerConfig(TokenStore tokenStore,
        UserApprovalHandler userApprovalHandler,
        @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
        JwtAccessTokenConverter accessTokenConverter,
        SecurityInfoTokenEnhancer securityInfoTokenEnhancer,
        ClientDetailsService clientDetailsService,
        UserDetailsService userDetailsService)
  {
    this.tokenStore = tokenStore;
    this.userApprovalHandler = userApprovalHandler;
    this.authenticationManager = authenticationManager;
    this.clientDetailsService = clientDetailsService;
    this.userDetailsService = userDetailsService;
    this.accessTokenConverter = accessTokenConverter;
    this.securityInfoTokenEnhancer = securityInfoTokenEnhancer;
  }

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
  {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(securityInfoTokenEnhancer, accessTokenConverter));

    super.configure(endpoints);
    endpoints
        .tokenStore(tokenStore)
        .tokenEnhancer(tokenEnhancerChain)
        .accessTokenConverter(accessTokenConverter)
        .userApprovalHandler(userApprovalHandler)
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception
  {
    clients.withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer)
  {
    oauthServer
        .realm(REALM)
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .authenticationEntryPoint(new OAuth2AuthenticationEntryPoint())
        .accessDeniedHandler(new CustomAuthenticationFailureHandler());
    oauthServer.addTokenEndpointAuthenticationFilter(authorizationServerEndpointCorsFilter());
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices()
  {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore);
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  @Bean
  public CorsFilter authorizationServerEndpointCorsFilter()
  {
    // Open Up Auth Endpoint to everyone
    CorsConfiguration oAuthConfig = new CorsConfiguration();
    oAuthConfig.setAllowCredentials(true);
    oAuthConfig.addAllowedOrigin("*");
    oAuthConfig.addAllowedHeader("*");
    oAuthConfig.addAllowedMethod("OPTIONS");
    oAuthConfig.addAllowedMethod("POST");

    CorsConfiguration globalOAuthConfig = new CorsConfiguration();
    globalOAuthConfig.setAllowCredentials(true);
    globalOAuthConfig.addAllowedOrigin("*");
    globalOAuthConfig.addAllowedHeader("*");
    globalOAuthConfig.addAllowedMethod("OPTIONS");
    globalOAuthConfig.addAllowedMethod("GET");
    globalOAuthConfig.addAllowedMethod("POST");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/oauth/token", oAuthConfig);
    source.registerCorsConfiguration("/oauth/**", globalOAuthConfig);

    return new CorsFilter(source);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
