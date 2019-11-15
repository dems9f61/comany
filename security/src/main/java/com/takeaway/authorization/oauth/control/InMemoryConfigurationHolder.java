package com.takeaway.authorization.oauth.control;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.takeaway.authorization.oauth.entity.CustomUserDetails;
import com.takeaway.authorization.oauth.entity.UserInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User: StMinko Date: 12.11.2019 Time: 15:21
 *
 * <p>
 */
@Configuration
public class InMemoryConfigurationHolder
{
  // =========================== Class Variables ===========================

  private static final int ONE_DAY = 60 * 60 * 24;

  private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  // ===========================  public  Methods  =========================

  @Bean
  @Profile("INTEGRATION")
  @Primary
  public ClientDetailsService inMemoryClientDetailService()
  {
    InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
    Map<String, BaseClientDetails> clients = Maps.newHashMap();
    BaseClientDetails firstClient = new BaseClientDetails("client", "", "read,write", "client_credentials,password,refresh_token", "", "");
    firstClient.setClientSecret("{noop}secret");
    firstClient.setAccessTokenValiditySeconds(ONE_DAY);
    firstClient.setRefreshTokenValiditySeconds(THIRTY_DAYS);
    clients.put("client", firstClient);

    BaseClientDetails secondClient = new BaseClientDetails("clientWithBadScope", "", "bad_scope", "password,refresh_token", "", "");
    secondClient.setClientSecret("{noop}secret");
    secondClient.setAccessTokenValiditySeconds(ONE_DAY);
    secondClient.setRefreshTokenValiditySeconds(THIRTY_DAYS);
    clients.put("clientWithBadScope", secondClient);

    clientDetailsService.setClientDetailsStore(clients);
    return clientDetailsService;
  }

  @Bean
  @Profile("INTEGRATION")
  @Primary
  public UserDetailsService inMemoryUserDetailsService()
  {
    List<UserDetails> users = Lists.newArrayList();
    List<GrantedAuthority> firstUserAuthorities = new LinkedList<>();
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_READ"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_CREATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_UPDATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_DELETE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_AUDIT_TRAIL"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_READ"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_CREATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_UPDATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_DELETE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_AUDIT_TRAIL"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_READ"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_CREATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_UPDATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_DELETE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_AUDIT_TRAIL"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_READ"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_DELETE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_CREATE"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_AUDIT_TRAIL"));
    firstUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_UPDATE"));

    CustomUserDetails firstUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), firstUserAuthorities), "admin", "{noop}admin", true, firstUserAuthorities);
    users.add(firstUser);

    List<GrantedAuthority> secondUserAuthorities = new LinkedList<>();
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_READ"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_AUDIT_TRAIL"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_READ"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_AUDIT_TRAIL"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_READ"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_AUDIT_TRAIL"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_READ"));
    secondUserAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_AUDIT_TRAIL"));

    CustomUserDetails secondUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), secondUserAuthorities), "user", "{noop}user", true, secondUserAuthorities);
    users.add(secondUser);

    List<GrantedAuthority> thirdUserAuthorities = new LinkedList<>();
    thirdUserAuthorities.add(new SimpleGrantedAuthority("ROLE_USELESS_ROLE"));
    CustomUserDetails thirdUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), thirdUserAuthorities),
            "userWithNoRole",
            "{noop}user",
            true,
            thirdUserAuthorities);
    users.add(thirdUser);

    return new InMemoryUserDetailsManager(users)
    {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
      {
        UserDetails userDetails = super.loadUserByUsername(username);
        return users.stream()
              .filter(user -> user.getUsername().equals(userDetails.getUsername()))
              .findFirst()
              .orElseThrow(() -> new UsernameNotFoundException(username));
      }
    };
  }

  @Profile("INTEGRATION")
  @Bean
  public TokenStore inMemoryTokenStore()
  {
    return new InMemoryTokenStore();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
