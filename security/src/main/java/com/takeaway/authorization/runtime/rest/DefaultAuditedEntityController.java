package com.takeaway.authorization.runtime.rest;

import com.takeaway.authorization.runtime.auditing.boundary.AuditedEntityService;
import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.persistence.AuditedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * User: StMinko Date: 16.10.2019 Time: 14:35
 *
 * <p>
 */
@Validated
public class DefaultAuditedEntityController<SERVICE extends AuditedEntityService<ENTITY, ID>, ENTITY extends AuditedEntity<ID>, ID extends Serializable>
        extends DefaultEntityController<SERVICE, ENTITY, ID> implements DefaultAuditedEntityControllerCapable<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public DefaultAuditedEntityController(SERVICE service)
    {
        super(service);
    }

    // ===========================  public  Methods  =========================

    @Override
    public Page<Revision<Long, ENTITY>> findRevisions(@NotNull ID id, @NotNull Pageable pageable)
    {
        return getService().findHistory(id, pageable);
    }

    @Override
    public Page<AuditTrail<ID, ENTITY>> findAuditTrails(@NotNull ID id, @NotNull Pageable pageable, @NotNull Class<? extends ENTITY> entityClass)
    {
        List<AuditTrail<ID, ENTITY>> auditTrails = getService().findAuditTrails(id, entityClass);
        return new PageImpl<>(auditTrails, pageable, auditTrails.size());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
