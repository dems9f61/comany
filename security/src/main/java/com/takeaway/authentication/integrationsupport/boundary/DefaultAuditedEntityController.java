package com.takeaway.authentication.integrationsupport.boundary;

import com.takeaway.authentication.integrationsupport.entity.AuditTrail;
import com.takeaway.authentication.integrationsupport.entity.AuditedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko
 * Date: 16.10.2019
 * Time: 14:31
 * <p/>
 */
public interface DefaultAuditedEntityController<ENTITY extends AuditedEntity<ID>, ID extends Serializable> extends DefaultEntityController<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @GetMapping("/{id}/revisions")
    @ResponseStatus(HttpStatus.OK)
    Page<Revision<Long, ENTITY>> findRevisions(@PathVariable @NotNull ID id, @NotNull @PageableDefault(50) Pageable pageable);

    @GetMapping("/{id}/auditTrails")
    @ResponseStatus(HttpStatus.OK)
    ApiResponsePage<AuditTrail<ID, ENTITY>> findAuditTrails(@PathVariable @NotNull ID id,
                                                            @PageableDefault(50) @NotNull Pageable pageable);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
