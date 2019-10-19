package com.takeaway.authentication.role.boundary;

import com.takeaway.authentication.integrationsupport.boundary.ApiVersions;
import com.takeaway.authentication.integrationsupport.control.AbstractDefaultAuditedEntityController;
import com.takeaway.authentication.role.control.RoleService;
import com.takeaway.authentication.role.entity.Role;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 14.10.2019
 * Time: 16:28
 * <p/>
 */
@Slf4j
@Validated
@RestController
@Api(value = "User role service: Operations pertaining to the user role service interface")
@RequestMapping(value = RoleController.BASE_URI,
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE },
        consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class RoleController extends AbstractDefaultAuditedEntityController<RoleService, Role, UUID>
{
    // =========================== Class Variables ===========================

    public static final String BASE_URI = ApiVersions.V1 + "/roles";

    // =============================  Variables  =============================

    @Getter
    private final RoleService service;

    @Getter
    private final Class<? extends Role> entityClass = Role.class;

    // ============================  Constructors  ===========================

    @Autowired
    public RoleController(@NotNull RoleService roleService)
    {
        this.service = roleService;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
