package com.takeaway.authorization.oauth.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * User: StMinko Date: 06.11.2019 Time: 17:22
 *
 * <p>
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final TokenStore tokenStore;

  // ============================  Constructors  ===========================

  @Autowired
  public ResourceServerConfig(TokenStore tokenStore)
  {
    this.tokenStore = tokenStore;
  }

  // ===========================  public  Methods  =========================

  @Override
  public void configure(HttpSecurity http) throws Exception
  {
    http.anonymous().disable()
        .authorizeRequests().anyRequest().authenticated()
        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception
  {
    super.configure(resources);
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore);
    resources.tokenServices(defaultTokenServices);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
