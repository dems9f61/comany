package com.takeaway.authorization.integrationsupport.control;

import com.takeaway.authorization.integrationsupport.entity.AuditQueryUtils;
import com.takeaway.authorization.integrationsupport.entity.AuditTrail;
import com.takeaway.authorization.integrationsupport.entity.AuditedEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: StMinko Date: 16.10.2019 Time: 14:16
 *
 * <p>
 */
@Validated
@Slf4j
public abstract class AbstractDefaultAuditedEntityService<
        REPOSITORY extends JpaAuditedSpecificationRepository<ENTITY, ID>, ENTITY extends AuditedEntity<ID>, ID extends Serializable>
    extends AbstractDefaultEntityService<REPOSITORY, ENTITY, ID> implements AuditedEntityService<ENTITY, ID>
{
  // =========================== Class Variables ===========================

  @PersistenceContext
  private EntityManager entityManager;

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
    LOGGER.info("{}.findRevisions ( {} , {} )", this.getClass().getSimpleName(), id, pageable);
    return getRepository().findRevisions(id, pageable);
  }

  @Transactional(readOnly = true)
  public List<AuditTrail<ID, ENTITY>> findAuditTrails(ID entityId, Class<? extends ENTITY> entityClass)
  {
    LOGGER.info("{}.findAuditTrails ( {} , {} )", this.getClass().getSimpleName(), entityId, entityClass);
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(entityClass, false, true).add(AuditEntity.id().eq(entityId));
    return AuditQueryUtils.getAuditQueryResults(auditQuery, entityClass).stream()
          .map(auditQueryResult -> {
              ENTITY entity = auditQueryResult.getEntity();
              return new AuditTrail<>(entity, auditQueryResult.getRevision().getRevisionNumber(), auditQueryResult.getType());
            })
        // And collect the Results:
        .collect(Collectors.toList());
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
