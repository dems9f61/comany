package com.takeaway.authorization.runtime.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: StMinko Date: 30.10.2019 Time: 17:25
 *
 * <p>
 */
@Data
public class CustomUserDetails implements UserDetails
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final Set<? extends GrantedAuthority> authorities;

    private final String username;

    private final String password;

    private final boolean enabled;

    private final UserInformation userInformation;

    // ============================  Constructors  ===========================

    public CustomUserDetails(UserInformation userInformation, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities)
    {
        this.authorities = new HashSet<>(authorities);
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.userInformation = userInformation;
    }

    // ===========================  public  Methods  =========================

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return isEnabled();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
