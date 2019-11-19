package com.takeaway.authorization.oauth.boundary;

import com.takeaway.authorization.oauth.control.EntitySecurityHolder;
import com.takeaway.authorization.oauth.control.SecurityInfoTokenEnhancer;
import com.takeaway.authorization.oauth.control.SpringSecuritySecurityProvider;
import com.takeaway.authorization.oauth.entity.CustomJwtTokenStore;
import com.takeaway.authorization.oauth.entity.CustomUserDetailsEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * User: StMinko Date: 06.11.2019 Time: 16:54
 *
 * <p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final ClientDetailsService clientDetailsService;

  private final UserDetailsService userDetailsService;

  @Value("${oauth.resourceserver.verifierKey}")
   private String verifierKey;

   @Value("${oauth.authorizationserver.signingKey}")
   private String signingKey;

  @Autowired
  public SecurityConfig(ClientDetailsService clientDetailsService, UserDetailsService userDetailsService)
  {
    this.clientDetailsService = clientDetailsService;
    this.userDetailsService = userDetailsService;
    EntitySecurityHolder.set(new SpringSecuritySecurityProvider());
  }

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception
  {
    return super.authenticationManagerBean();
  }

  @Bean
  @Autowired
  public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore)
  {
    TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
    handler.setTokenStore(tokenStore);
    handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
    handler.setClientDetailsService(clientDetailsService);
    return handler;
  }

  @Bean
  @Autowired
  public ApprovalStore approvalStore(TokenStore tokenStore)
  {
    TokenApprovalStore store = new TokenApprovalStore();
    store.setTokenStore(tokenStore);
    return store;
  }

  // =================  protected/package local  Methods ===================

  @Override
  @Order(Ordered.HIGHEST_PRECEDENCE)
  protected void configure(HttpSecurity http) throws Exception
  {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/oauth/token")
        .permitAll()
        .antMatchers("/api/**")
        .authenticated()
        .and()
        .httpBasic()
        .realmName(AuthorizationServerConfig.REALM);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception
  {
    auth.userDetailsService(userDetailsService);
  }

  //  @Primary
  //  @Profile("!INTEGRATION")
  //  @Bean
  //  public JdbcTokenStore tokenStore(DataSource dataSource)
  //  {
  //    return new JdbcTokenStore(dataSource);
  //  }

  @Bean
//  @Profile("!INTEGRATION")
  public TokenStore tokenStore()
  {
    return new CustomJwtTokenStore(accessTokenConverter());
  }

  @Bean
  //  @Profile("!INTEGRATION")
  public JwtAccessTokenConverter accessTokenConverter()
  {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(signingKey);
    converter.setVerifierKey(verifierKey);
    return converter;
  }


  @Bean
  //  @Profile("!INTEGRATION")
  public CustomUserDetailsEnhancer customUserDetailsEnhancer () {
      return new CustomUserDetailsEnhancer();
  }

  @Bean
  //  @Profile("!INTEGRATION") 
  public SecurityInfoTokenEnhancer tokenEnhancer(CustomUserDetailsEnhancer customUserDetailsEnhancer)
  {
    return new SecurityInfoTokenEnhancer(customUserDetailsEnhancer);
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
