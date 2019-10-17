package com.takeaway.authentication.permission.boundary;

import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.boundary.ApiVersions;
import com.takeaway.authentication.integrationsupport.control.AbstractDefaultAuditedEntityController;
import com.takeaway.authentication.permission.control.PermissionService;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.permission.entity.PermissionHistory;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 11:55
 * <p/>
 */
@Slf4j
@Validated
@RestController
@Api(value = "User permission service: Operations pertaining to the user permission service interface")
@RequestMapping(value = PermissionController.BASE_URI,
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class PermissionController extends AbstractDefaultAuditedEntityController<PermissionService, Permission, UUID>
{
    // =========================== Class Variables ===========================

    static final String BASE_URI = ApiVersions.V1 + "/permissions";

    // =============================  Variables  =============================

    @Getter
    private final PermissionService service;

    // ============================  Constructors  ===========================

    @Autowired
    public PermissionController(@NotNull PermissionService permissionService)
    {
        this.service = permissionService;
    }

    // ===========================  public  Methods  =========================

    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    ApiResponsePage<PermissionHistory> revisions(@PathVariable @NotNull UUID id,  @PageableDefault(50) @NotNull Pageable pageable){
        List<PermissionHistory> permissionHistories = service.listRevisions(id);
        return new ApiResponsePage<>(permissionHistories, pageable, permissionHistories.size());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
