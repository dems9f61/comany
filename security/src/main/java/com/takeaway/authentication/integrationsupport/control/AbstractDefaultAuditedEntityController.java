package com.takeaway.authentication.integrationsupport.control;

import com.takeaway.authentication.integrationsupport.boundary.AbstractDefaultEntityController;
import com.takeaway.authentication.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authentication.integrationsupport.boundary.DefaultAuditedEntityController;
import com.takeaway.authentication.integrationsupport.entity.AuditedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko
 * Date: 16.10.2019
 * Time: 14:35
 * <p/>
 */
public abstract class AbstractDefaultAuditedEntityController<SERVICE extends AuditedEntityService<ENTITY, ID>, ENTITY extends AuditedEntity<ID>, ID extends Serializable>
        extends AbstractDefaultEntityController<SERVICE, ENTITY, ID> implements DefaultAuditedEntityController<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public ApiResponsePage<Revision<Long, ENTITY>> findHistory(@PathVariable @NotNull ID id, @NotNull @PageableDefault(50) Pageable pageable)
    {
        Page<Revision<Long, ENTITY>> history = getService().findHistory(id, pageable);
        return new ApiResponsePage<>(history.getContent(), pageable, history.getTotalElements());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
