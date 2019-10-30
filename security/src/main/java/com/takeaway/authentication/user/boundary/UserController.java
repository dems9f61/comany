package com.takeaway.authentication.user.boundary;

import com.takeaway.authentication.integrationsupport.boundary.AbstractDefaultAuditedEntityController;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.boundary.ApiVersions;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.user.control.UserService;
import com.takeaway.authentication.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 21.10.2019 Time: 11:48
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = UserController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class UserController extends AbstractDefaultAuditedEntityController<UserService, User, UUID>
{
  // =========================== Class Variables ===========================

  public static final String BASE_URI = ApiVersions.V1 + "/users";

  // =============================  Variables  =============================

  @Getter
  private final UserService service;

  // ============================  Constructors  ===========================

  @Autowired
  public UserController(@NotNull UserService userService)
  {
    this.service = userService;
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @GetMapping("/{id}/permissions")
  @ResponseStatus(HttpStatus.OK)
  Page<Permission> findAllPermissionByUser(@NotNull @PathVariable UUID id, @NotNull @PageableDefault(50) Pageable pageable)
  {
    Page<Permission> permissions = service.findAllPermissionByUser(id, pageable);
    return new ApiResponsePage<>(permissions.getContent(), pageable, permissions.getTotalElements());
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
