package com.takeaway.authorization.oauth.boundary;

import com.takeaway.authorization.oauth.control.EntitySecurityHolder;
import com.takeaway.authorization.oauth.control.SpringSecuritySecurityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * User: StMinko Date: 06.11.2019 Time: 16:54
 *
 * <p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final ClientDetailsService clientDetailsService;

  private final UserDetailsService userDetailsService;

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

  @Primary
  @Profile("!INTEGRATION")
  @Bean
  public JdbcTokenStore tokenStore(DataSource dataSource)
  {
    return new JdbcTokenStore(dataSource);
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
