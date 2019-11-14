package com.takeaway.authorization.rest.boundary;

import com.takeaway.authorization.auditing.boundary.AuditedEntityService;
import com.takeaway.authorization.auditing.entity.AuditTrail;
import com.takeaway.authorization.persistence.boundary.AuditedEntity;
import com.takeaway.authorization.view.boundary.ResponsePage;
import org.springframework.data.domain.Page;
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
        extends DefaultEntityController<SERVICE, ENTITY, ID> implements DefaultAuditedEntityControllerCapableCapable<ENTITY, ID>
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
  public ResponsePage<Revision<Long, ENTITY>> findRevisions(@NotNull ID id, @NotNull Pageable pageable)
  {
    Page<Revision<Long, ENTITY>> history = getService().findHistory(id, pageable);
      return new ResponsePage<>(history.getContent(), pageable, history.getTotalElements());
  }

  @Override
  public ResponsePage<AuditTrail<ID, ENTITY>> findAuditTrails(@NotNull ID id,
                                                              @NotNull Pageable pageable,
                                                              @NotNull Class<? extends ENTITY> entityClass)
  {
    List<AuditTrail<ID, ENTITY>> auditTrails = getService().findAuditTrails(id, entityClass);
      return new ResponsePage<>(auditTrails, pageable, auditTrails.size());
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
