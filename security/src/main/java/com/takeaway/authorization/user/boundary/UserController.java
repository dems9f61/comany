package com.takeaway.authorization.user.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.permission.entity.Permission;
import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.rest.ApiVersions;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.DefaultAuditedEntityController;
import com.takeaway.authorization.runtime.rest.ResponsePage;
import com.takeaway.authorization.user.control.UserService;
import com.takeaway.authorization.user.entity.User;
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
 * User: StMinko Date: 21.10.2019 Time: 11:48
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = UserController.BASE_URI,
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class UserController
{
    // =========================== Class Variables ===========================

    public static final String BASE_URI = ApiVersions.V1 + "/users";

    // =============================  Variables  =============================

    private final DefaultAuditedEntityController<UserService, User, UUID> controllerDelegator;

    // ============================  Constructors  ===========================

    @Autowired
    public UserController(@NotNull UserService userService)
    {
        this.controllerDelegator = new DefaultAuditedEntityController<>(userService);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @PreAuthorize("hasRole('USER_READ') and #oauth2.hasScope('read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<User> findAll(@NotNull Pageable pageable)
    {
        return controllerDelegator.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER_READ') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    User findById(@PathVariable @NotNull UUID id)
    {
        return controllerDelegator.findById(id);
    }

    @PreAuthorize("hasRole('USER_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    Page<Revision<Long, User>> findRevisions(@PathVariable @NotNull UUID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        return controllerDelegator.findRevisions(id, pageable);
    }

    @PreAuthorize("hasRole('USER_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/auditTrails")
    @ResponseStatus(HttpStatus.OK)
    ResponsePage<AuditTrail<UUID, User>> findAuditTrails(@PathVariable @NotNull UUID id, @PageableDefault(50) @NotNull Pageable pageable)
    {
        return controllerDelegator.findAuditTrails(id, pageable, User.class);
    }

    @PreAuthorize("hasRole('USER_READ') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    Page<Permission> findAllPermissionByUser(@NotNull @PathVariable UUID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        Page<Permission> permissions = controllerDelegator.getService()
                                                          .findAllPermissionByUser(id, pageable);
        return new ResponsePage<>(permissions.getContent(), pageable, permissions.getTotalElements());
    }

    @PreAuthorize("hasRole('USER_CREATE') and #oauth2.hasScope('write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DataView.GET.class)
    ResponseEntity<User> create(@RequestBody @JsonView(DataView.POST.class) @NotNull User createRequest)
    {
        return controllerDelegator.create(createRequest);
    }

    @PreAuthorize("hasRole('USER_UPDATE') and #oauth2.hasScope('write')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    User doFullUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PUT.class) User fullUpdateRequest)
    {
        return controllerDelegator.doFullUpdate(id, fullUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_UPDATE') and #oauth2.hasScope('write')")
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    User doPartialUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PATCH.class) User partialUpdateRequest)
    {
        return controllerDelegator.doPartialUpdate(id, partialUpdateRequest);
    }

    @PreAuthorize("hasRole('USER_DELETE') and #oauth2.hasScope('write')")
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
