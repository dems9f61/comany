package com.takeaway.authorization.userrole.control;

import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
import com.takeaway.authorization.userrole.entity.UserRole;
import com.takeaway.exeption.boundary.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 30.10.2019 Time: 11:45
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class UserRoleService
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final UserRoleRepository repository;

    private final UserService userService;

    private final RoleService roleService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public Role assign(@NotNull UUID userId, @NotNull UUID roleId)
    {
        User user = userService.findByIdOrElseThrow(userId, BadRequestException.class);
        Role role = roleService.findByIdOrElseThrow(roleId, BadRequestException.class);

        return repository.findByUserAndPermission(userId, roleId)
                         .orElseGet(() -> {
                             UserRole userRole = new UserRole();
                             userRole.setRole(role);
                             userRole.setUser(user);
                             return repository.save(userRole);
                         })
                         .getRole();
    }

    public void unassign(@NotNull UUID userId, @NotNull UUID roleId)
    {
        userService.findByIdOrElseThrow(userId, BadRequestException.class);
        roleService.findByIdOrElseThrow(roleId, BadRequestException.class);
        repository.findByUserAndPermission(userId, roleId)
                  .ifPresent(repository::delete);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
