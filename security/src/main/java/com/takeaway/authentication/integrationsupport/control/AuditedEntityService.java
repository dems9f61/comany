package com.takeaway.authentication.integrationsupport.control;

import com.takeaway.authentication.integrationsupport.entity.AuditTrail;
import com.takeaway.authentication.integrationsupport.entity.AuditedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * User: StMinko Date: 16.10.2019 Time: 14:13
 *
 * <p>
 */
@Transactional(propagation = Propagation.REQUIRED)
@Validated
public interface AuditedEntityService<ENTITY extends AuditedEntity<ID>, ID extends Serializable> extends EntityService<ENTITY, ID>
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  Page<Revision<Long, ENTITY>> findHistory(@NotNull ID id, @NotNull Pageable pageable);

  @Transactional(readOnly = true)
  List<AuditTrail<ID, ENTITY>> findAuditTrails(@NotNull ID entityId, @NotNull Class<? extends ENTITY> entityClass);

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
