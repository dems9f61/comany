package com.takeaway.authentication.rolepermission.control;

import com.takeaway.authentication.integrationsupport.entity.ServiceException;
import com.takeaway.authentication.permission.control.PermissionService;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.role.control.RoleService;
import com.takeaway.authentication.role.entity.Role;
import com.takeaway.authentication.rolepermission.entity.RolePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 19.10.2019 Time: 16:49
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class RolePermissionService
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final RolePermissionRepository rolePermissionRepository;

  private final RoleService roleService;

  private final PermissionService permissionService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public Page<Permission> findAllByRole(@NotNull UUID roleId, @NotNull Pageable pageable)
  {
    return rolePermissionRepository.findAllByRole(roleId, pageable);
  }

  public Permission assign(@NotNull UUID roleId, @NotNull UUID permissionId) throws ServiceException
  {
    Role role = roleService
            .findById(roleId)
            .orElseThrow(() -> new ServiceException(ServiceException.Reason.SUB_RESOURCE_NOT_FOUND, String.format("Could not find a role by the specified id [%s]!", roleId)));
    Permission permission = permissionService
            .findById(permissionId)
            .orElseThrow(() -> new ServiceException(ServiceException.Reason.SUB_RESOURCE_NOT_FOUND,
                    String.format("Could not find a permission by the specified id [%s]!", permissionId)));
    return rolePermissionRepository
        .findByRoleAndPermission(roleId, permissionId)
        .orElseGet(() -> {
              RolePermission rolePermission = new RolePermission();
              rolePermission.setPermission(permission);
              rolePermission.setRole(role);
              return rolePermissionRepository.save(rolePermission);
            })
        .getPermission();
  }

  public void unassign(@NotNull UUID roleId, @NotNull UUID permissionId) throws ServiceException
  {
    roleService
        .findById(roleId)
        .orElseThrow(() -> new ServiceException(ServiceException.Reason.SUB_RESOURCE_NOT_FOUND, String.format("Could not find a role by the specified id [%s]!", roleId)));
    permissionService
        .findById(permissionId)
        .orElseThrow(() -> new ServiceException(ServiceException.Reason.SUB_RESOURCE_NOT_FOUND,
                String.format("Could not find a permission by the specified id [%s]!", permissionId)));
    rolePermissionRepository.findByRoleAndPermission(roleId, permissionId).ifPresent(rolePermissionRepository::delete);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
