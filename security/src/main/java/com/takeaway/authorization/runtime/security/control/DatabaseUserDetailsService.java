package com.takeaway.authorization.runtime.security.control;

import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.runtime.security.entity.CustomUserDetails;
import com.takeaway.authorization.runtime.security.entity.UserInformation;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Profile("!INTEGRATION")
@Service
@RequiredArgsConstructor
@Transactional
public class DatabaseUserDetailsService implements UserDetailsService
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
            String permissionName = permission.getName();
            permissionName = !permissionName.startsWith("ROLE_") ? "ROLE_" + permissionName : permissionName;
            permissions.add(new SimpleGrantedAuthority(permissionName));
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(new UserInformation(user.getId(), permissions),
                                                                    user.getUserName(),
                                                                    user.getPasswordHash(),
                                                                    true,
                                                                    permissions);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                                                                                                     null,
                                                                                                     customUserDetails.getAuthorities());
        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        return customUserDetails;
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
