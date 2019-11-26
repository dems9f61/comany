package com.takeaway.authorization.runtime.rest;

import com.takeaway.authorization.runtime.auditing.entity.AuditTrail;
import com.takeaway.authorization.runtime.persistence.AuditedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko Date: 16.10.2019 Time: 14:31
 * <p>
 */
@Validated
public interface DefaultAuditedEntityControllerCapable<ENTITY extends AuditedEntity<ID>, ID extends Serializable>
        extends DefaultEntityControllerCapable<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Page<Revision<Long, ENTITY>> findRevisions(@NotNull ID id, @NotNull Pageable pageable);

    ResponsePage<AuditTrail<ID, ENTITY>> findAuditTrails(@NotNull ID id, @NotNull Pageable pageable, @NotNull Class<? extends ENTITY> entityClass);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
