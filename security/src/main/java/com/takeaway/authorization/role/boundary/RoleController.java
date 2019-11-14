package com.takeaway.authorization.role.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.auditing.entity.AuditTrail;
import com.takeaway.authorization.rest.boundary.ApiVersions;
import com.takeaway.authorization.rest.boundary.DefaultAuditedEntityController;
import com.takeaway.authorization.role.control.RoleService;
import com.takeaway.authorization.role.entity.Role;
import com.takeaway.authorization.view.boundary.DataView;
import com.takeaway.authorization.view.boundary.ResponsePage;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 14.10.2019 Time: 16:28
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@Api(value = "User role service: Operations pertaining to the user role service interface")
@RequestMapping(value = RoleController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class RoleController
{
  // =========================== Class Variables ===========================

  public static final String BASE_URI = ApiVersions.V1 + "/roles";

  // =============================  Variables  =============================

    private final DefaultAuditedEntityController<RoleService, Role, UUID> controllerDelegator;

  // ============================  Constructors  ===========================

  @Autowired
  public RoleController(@NotNull RoleService roleService)
  {
      this.controllerDelegator = new DefaultAuditedEntityController<>(roleService);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

    @PreAuthorize("hasRole('USER_ROLE_READ') and #oauth2.hasScope('read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<Role> findAll(@NotNull Pageable pageable)
    {
        return controllerDelegator.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER_ROLE_READ') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Role findById(@PathVariable @NotNull UUID id)
    {
        return controllerDelegator.findById(id);
    }

    @PreAuthorize("hasRole('USER_ROLE_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    Page<Revision<Long, Role>> findRevisions(@PathVariable @NotNull UUID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        return controllerDelegator.findRevisions(id, pageable);
    }

    @PreAuthorize("hasRole('USER_ROLE_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/auditTrails")
    @ResponseStatus(HttpStatus.OK)
    ResponsePage<AuditTrail<UUID, Role>> findAuditTrails(@PathVariable @NotNull UUID id, @PageableDefault(50) @NotNull Pageable pageable)
    {
        return controllerDelegator.findAuditTrails(id, pageable, Role.class);
    }

    @PreAuthorize("hasRole('USER_ROLE_CREATE') and #oauth2.hasScope('write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DataView.GET.class)
    ResponseEntity<Role> create(@RequestBody @JsonView(DataView.POST.class) @NotNull Role createRequest)
    {
        return controllerDelegator.create(createRequest);
    }

    @PreAuthorize("hasRole('USER_ROLE_UPDATE') and #oauth2.hasScope('write')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Role doFullUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PUT.class) Role fullUpdateRequest)
    {
        return controllerDelegator.doFullUpdate(id, fullUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_ROLE_UPDATE') and #oauth2.hasScope('write')")
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Role doPartialUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PATCH.class) Role partialUpdateRequest)
    {
        return controllerDelegator.doPartialUpdate(id, partialUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_ROLE_DELETE') and #oauth2.hasScope('write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable @NotNull UUID id)
    {
        controllerDelegator.delete(id);
    }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
