package com.takeaway.authorization.oauthclient.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.oauthclient.control.OauthClientService;
import com.takeaway.authorization.oauthclient.entity.OauthClient;
import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.rest.ApiVersions;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.DefaultAuditedEntityController;
import com.takeaway.authorization.runtime.rest.ResponsePage;
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
 * User: StMinko Date: 06.11.2019 Time: 11:30
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = OauthClientController.BASE_URI,
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class OauthClientController
{
    // =========================== Class Variables ===========================

    public static final String BASE_URI = ApiVersions.V1 + "/oauth-clients";

    // =============================  Variables  =============================

    private final DefaultAuditedEntityController<OauthClientService, OauthClient, UUID> controllerDelegator;

    // ============================  Constructors  ===========================

    @Autowired
    public OauthClientController(@NotNull OauthClientService oAuthClientService)
    {
        this.controllerDelegator = new DefaultAuditedEntityController<>(oAuthClientService);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @PreAuthorize("hasRole('OAUTH_CLIENT_READ') and #oauth2.hasScope('read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<OauthClient> findAll(@NotNull Pageable pageable)
    {
        return controllerDelegator.findAll(pageable);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_READ') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    OauthClient findById(@PathVariable @NotNull UUID id)
    {
        return controllerDelegator.findById(id);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    Page<Revision<Long, OauthClient>> findRevisions(@PathVariable @NotNull UUID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        return controllerDelegator.findRevisions(id, pageable);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_AUDIT_TRAIL') and #oauth2.hasScope('read')")
    @GetMapping("/{id}/auditTrails")
    @ResponseStatus(HttpStatus.OK)
    ResponsePage<AuditTrail<UUID, OauthClient>> findAuditTrails(@PathVariable @NotNull UUID id, @PageableDefault(50) @NotNull Pageable pageable)
    {
        return controllerDelegator.findAuditTrails(id, pageable, OauthClient.class);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_CREATE') and #oauth2.hasScope('write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DataView.GET.class)
    ResponseEntity<OauthClient> create(@RequestBody @JsonView(DataView.POST.class) @NotNull OauthClient createRequest)
    {
        return controllerDelegator.create(createRequest);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_UPDATE') and #oauth2.hasScope('write')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    OauthClient doFullUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PUT.class) OauthClient fullUpdateRequest)
    {
        return controllerDelegator.doFullUpdate(id, fullUpdateRequest);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_UPDATE') and #oauth2.hasScope('write')")
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(DataView.GET.class)
    OauthClient doPartialUpdate(@PathVariable @NotNull UUID id, @RequestBody @JsonView(DataView.PATCH.class) OauthClient partialUpdateRequest)
    {
        return controllerDelegator.doPartialUpdate(id, partialUpdateRequest);
    }

    @PreAuthorize("hasRole('OAUTH_CLIENT_DELETE') and #oauth2.hasScope('write')")
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
