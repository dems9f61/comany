package com.takeaway.authorization.rolepermission.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.errorhandling.control.ServiceExceptionTranslator;
import com.takeaway.authorization.errorhandling.entity.ServiceException;
import com.takeaway.authorization.json.boundary.DataView;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.role.boundary.RoleController;
import com.takeaway.authorization.rolepermission.control.RolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 19.10.2019 Time: 17:17
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = RolePermissionController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class RolePermissionController implements ServiceExceptionTranslator
{
  // =========================== Class Variables ===========================

  static final String BASE_URI = RoleController.BASE_URI + "/{roleId}/permissions";

  // =============================  Variables  =============================

  private final RolePermissionService rolePermissionService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

    @PreAuthorize("hasRole('USER_ROLE_UPDATE') and #oauth2.hasScope('write')")
    @PostMapping(value = "/{permissionId}")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DataView.GET.class)
    Permission assign(@NotNull @PathVariable UUID roleId, @NotNull @PathVariable UUID permissionId)
    {
        try
        {
            return rolePermissionService.assign(roleId, permissionId);
        }
        catch (ServiceException caught)
        {
            throw translateIntoApiException(caught);
        }
    }

    @PreAuthorize("hasRole('USER_ROLE_UPDATE') and #oauth2.hasScope('write')")
    @DeleteMapping(value = "/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unassign(@NotNull @PathVariable UUID roleId, @NotNull @PathVariable UUID permissionId)
    {
        try
        {
            rolePermissionService.unassign(roleId, permissionId);
        }
        catch (ServiceException caught)
        {
            throw translateIntoApiException(caught);
        }
    }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
