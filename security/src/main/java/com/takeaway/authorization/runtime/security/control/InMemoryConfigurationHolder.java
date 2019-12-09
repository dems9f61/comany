package com.takeaway.authorization.runtime.security.control;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.takeaway.authorization.runtime.security.entity.CustomUserDetails;
import com.takeaway.authorization.runtime.security.entity.UserInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
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
@RequiredArgsConstructor
public class InMemoryConfigurationHolder
{
    // =========================== Class Variables ===========================

    private static final int ONE_DAY = 60 * 60 * 24;

    private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

    // =============================  Variables  =============================

    private final PasswordEncoder passwordEncoder;

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
        firstClient.setClientSecret(passwordEncoder.encode("secret"));
        firstClient.setAccessTokenValiditySeconds(ONE_DAY);
        firstClient.setRefreshTokenValiditySeconds(THIRTY_DAYS);
        clients.put("client", firstClient);

        BaseClientDetails secondClient = new BaseClientDetails("clientWithBadScope", "", "bad_scope", "client_credentials,password,refresh_token", "", "");
        secondClient.setClientSecret(passwordEncoder.encode("secret"));
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
        List<GrantedAuthority> allAuthorities = new LinkedList<>();
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_READ"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_CREATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_UPDATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_DELETE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_AUDIT_TRAIL"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_READ"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_CREATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_UPDATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_DELETE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_AUDIT_TRAIL"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_READ"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_CREATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_UPDATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_DELETE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_AUDIT_TRAIL"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_READ"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_DELETE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_CREATE"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_AUDIT_TRAIL"));
        allAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_UPDATE"));

        CustomUserDetails admintUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), allAuthorities),
                        "admin",
                        passwordEncoder.encode("admin"),
                        true,
                        allAuthorities);
        users.add(admintUser);

        List<GrantedAuthority> readAuthorities = new LinkedList<>();
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_READ"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_AUDIT_TRAIL"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_READ"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_ROLE_AUDIT_TRAIL"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_READ"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_PERMISSION_AUDIT_TRAIL"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_READ"));
        readAuthorities.add(new SimpleGrantedAuthority("ROLE_OAUTH_CLIENT_AUDIT_TRAIL"));

        CustomUserDetails readUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), readAuthorities),
                        "user",
                        passwordEncoder.encode("user"),
                        true,
                        readAuthorities);
        users.add(readUser);

        List<GrantedAuthority> uselessAuthorities = new LinkedList<>();
        uselessAuthorities.add(new SimpleGrantedAuthority("ROLE_USELESS_ROLE"));
        CustomUserDetails uselessUser = new CustomUserDetails(new UserInformation(UUID.randomUUID(), uselessAuthorities),
                        "userWithNoRole",
                        passwordEncoder.encode("user"),
                        true,
                        uselessAuthorities);
        users.add(uselessUser);

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

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
