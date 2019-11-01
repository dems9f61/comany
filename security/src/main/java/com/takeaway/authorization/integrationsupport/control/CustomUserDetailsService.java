package com.takeaway.authorization.integrationsupport.control;

import com.takeaway.authorization.integrationsupport.entity.CustomUserDetails;
import com.takeaway.authorization.integrationsupport.entity.UserInformation;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: StMinko Date: 30.10.2019 Time: 17:22
 * <p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final UserService userService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userService.findByUserName(username)
                               .orElseThrow(() -> new UsernameNotFoundException(String.format("No User found for username: [%s]", username)));
        Page<Permission> userPermissions = userService.findAllPermissionByUser(user.getId(), Pageable.unpaged());

        List<GrantedAuthority> permissions = new ArrayList<>(userPermissions.getNumberOfElements());
        for (Permission permission : userPermissions.getContent())
        {
            permissions.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return new CustomUserDetails(new UserInformation(user.getId(), permissions), user.getUserName(), user.getPasswordHash(), true, permissions);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
