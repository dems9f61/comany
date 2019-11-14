package com.takeaway.authorization.userrole.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.errorhandling.control.ServiceExceptionTranslator;
import com.takeaway.authorization.errorhandling.entity.ServiceException;
import com.takeaway.authorization.json.boundary.DataView;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.user.boundary.UserController;
import com.takeaway.authorization.userrole.control.UserRoleService;
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
 * User: StMinko Date: 30.10.2019 Time: 12:01
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserRoleController.BASE_URI,
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class UserRoleController implements ServiceExceptionTranslator
{
  // =========================== Class Variables ===========================

  static final String BASE_URI = UserController.BASE_URI + "/{userId}/roles";

  // =============================  Variables  =============================

  private final UserRoleService userRoleService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @PreAuthorize("hasRole('USER_UPDATE') and #oauth2.hasScope('write')")
  @PostMapping(value = "/{roleId}")
  @ResponseStatus(HttpStatus.CREATED)
  @JsonView(DataView.GET.class)
  Role assign(@NotNull @PathVariable UUID userId, @NotNull @PathVariable UUID roleId)
  {
    try
    {
      return userRoleService.assign(userId, roleId);
    }
    catch (ServiceException caught)
    {
      throw translateIntoApiException(caught);
    }
  }

  @PreAuthorize("hasRole('USER_UPDATE') and #oauth2.hasScope('write')")
  @DeleteMapping(value = "/{roleId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void unassign(@NotNull @PathVariable UUID userId, @NotNull @PathVariable UUID roleId)
  {
    try
    {
      userRoleService.unassign(userId, roleId);
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
