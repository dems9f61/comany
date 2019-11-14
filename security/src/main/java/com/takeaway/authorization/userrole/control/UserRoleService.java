package com.takeaway.authorization.userrole.control;

import com.takeaway.authorization.errorhandling.entity.ServiceException;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
import com.takeaway.authorization.userrole.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.takeaway.authorization.errorhandling.entity.ServiceException.Reason.SUB_RESOURCE_NOT_FOUND;

/**
 * User: StMinko Date: 30.10.2019 Time: 11:45
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

    public Role assign(@NotNull UUID userId, @NotNull UUID roleId) throws ServiceException
    {
        User user = userService.findById(userId)
                               .orElseThrow(() -> new ServiceException(SUB_RESOURCE_NOT_FOUND,
                                                                       String.format("Could not find a user by the specified id [%s]!", userId)));

        Role role = roleService.findById(roleId)
                               .orElseThrow(() -> new ServiceException(SUB_RESOURCE_NOT_FOUND,
                                                                       String.format("Could not find a role by the specified id [%s]!", roleId)));

        return repository.findByUserAndPermission(userId, roleId)
                         .orElseGet(() -> {
                             UserRole userRole = new UserRole();
                             userRole.setRole(role);
                             userRole.setUser(user);
                             return repository.save(userRole);
                         })
                         .getRole();
    }

    public void unassign(@NotNull UUID userId, @NotNull UUID roleId) throws ServiceException
    {
        userService.findById(userId)
                   .orElseThrow(() -> new ServiceException(SUB_RESOURCE_NOT_FOUND,
                                                           String.format("Could not find a user by the specified id [%s]!", userId)));
        roleService.findById(roleId)
                   .orElseThrow(() -> new ServiceException(ServiceException.Reason.SUB_RESOURCE_NOT_FOUND,
                                                           String.format("Could not find a role by the specified id [%s]!", roleId)));

        repository.findByUserAndPermission(userId, roleId)
                  .ifPresent(repository::delete);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
