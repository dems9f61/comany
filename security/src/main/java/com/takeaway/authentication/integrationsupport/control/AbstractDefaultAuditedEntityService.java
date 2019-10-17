package com.takeaway.authentication.integrationsupport.control;

import com.takeaway.authentication.integrationsupport.entity.AuditedEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.validation.annotation.Validated;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko
 * Date: 16.10.2019
 * Time: 14:16
 * <p/>
 */
@Validated
@Slf4j
public class AbstractDefaultAuditedEntityService<REPOSITORY extends JpaAuditedSpecificationRepository<ENTITY, ID>, ENTITY extends AuditedEntity<ID>, ID extends Serializable>
        extends AbstractDefaultEntityService<REPOSITORY, ENTITY, ID> implements AuditedEntityService<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public AbstractDefaultAuditedEntityService(REPOSITORY repository, Validator validator)
    {
        super(repository, validator);
    }

    // ===========================  public  Methods  =========================

    @Override
    public Page<Revision<Long, ENTITY>> findHistory(@NotNull ID id, @NotNull Pageable pageable)
    {
        LOGGER.info("{}.findHistory ( {} , {} )",
                    this.getClass()
                        .getSimpleName(),
                    id,
                    pageable);
        return getRepository().findRevisions(id, pageable);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
