package com.takeaway.authorization.rolepermission.control;

import com.takeaway.authorization.permission.control.PermissionService;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.rolepermission.entity.RolePermission;
import com.takeaway.exeption.boundary.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public Permission assign(@NotNull UUID roleId, @NotNull UUID permissionId)
  {
    Role role = roleService.findByIdOrElseThrow(roleId, BadRequestException.class);
    Permission permission = permissionService.findByIdOrElseThrow(permissionId, BadRequestException.class);
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

  public void unassign(@NotNull UUID roleId, @NotNull UUID permissionId)
  {
    roleService.findByIdOrElseThrow(roleId, BadRequestException.class);
    permissionService.findByIdOrElseThrow(permissionId, BadRequestException.class);
    rolePermissionRepository.findByRoleAndPermission(roleId, permissionId).ifPresent(rolePermissionRepository::delete);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
