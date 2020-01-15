package com.takeaway.authorization.permission.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.permission.control.PermissionService;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.rest.ApiVersions;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.DefaultAuditedEntityController;
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
 * User: StMinko Date: 17.10.2019 Time: 11:55
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@Api(value = "User permission service: Operations pertaining to the user permission service interface")
@RequestMapping(value = PermissionController.BASE_URI,
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class PermissionController
{
    // =========================== Class Variables ===========================

    static final String BASE_URI = ApiVersions.V1 + "/permissions";

    // =============================  Variables  =============================

    private final DefaultAuditedEntityController<PermissionService, Permission, UUID> controllerDelegator;

    // ============================  Constructors  ===========================

    @Autowired
    public PermissionController(@NotNull PermissionService permissionService)
    {
        this.controllerDelegator = new DefaultAuditedEntityController<PermissionService, Permission, UUID>(permissionService);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @PreAuthorize("hasRole('USER_PERMISSION_READ') and #oauth2.hasScope('read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<Permission> findAll(@NotNull Pageable pageable)
    {
        return controllerDelegator.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_READ') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Permission findById(@PathVariable @NotNull UUID id)
    {
        return controllerDelegator.findById(id);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    Page<Revision<Long, Permission>> findRevisions(@PathVariable @NotNull UUID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        return controllerDelegator.findRevisions(id, pageable);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/auditTrails")
    @ResponseStatus(HttpStatus.OK)
    Page<AuditTrail<UUID, Permission>> findAuditTrails(@PathVariable @NotNull UUID id, @PageableDefault(50) @NotNull Pageable pageable)
    {
        return controllerDelegator.findAuditTrails(id, pageable, Permission.class);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_CREATE') and #oauth2.hasScope('write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DataView.GET.class)
    ResponseEntity<Permission> create(@RequestBody @JsonView(DataView.POST.class) @NotNull Permission createRequest)
    {
        return controllerDelegator.create(createRequest);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_UPDATE') and #oauth2.hasScope('write')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Permission doFullUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PUT.class) Permission fullUpdateRequest)
    {
        return controllerDelegator.doFullUpdate(id, fullUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_UPDATE') and #oauth2.hasScope('write')")
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    Permission doPartialUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PATCH.class) Permission partialUpdateRequest)
    {
        return controllerDelegator.doPartialUpdate(id, partialUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_PERMISSION_DELETE') and #oauth2.hasScope('write')")
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
