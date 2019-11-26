package com.takeaway.authorization.runtime.auditing.boundary;

import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.businessservice.boundary.EntityService;
import com.takeaway.authorization.runtime.persistence.AuditedEntity;
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
