package com.takeaway.authentication.permission.control;

import com.takeaway.authentication.integrationsupport.control.AbstractDefaultAuditedEntityService;
import com.takeaway.authentication.integrationsupport.entity.AuditQueryUtils;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.permission.entity.PermissionHistory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 11:49
 * <p/>
 */
@Service
public class PermissionService extends AbstractDefaultAuditedEntityService<PermissionRepository, Permission, UUID>
{

    @Autowired
    public PermissionService(PermissionRepository repository, Validator validator)
    {
        super(repository, validator);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<PermissionHistory> listRevisions(UUID entityId)
    {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery auditQuery = auditReader.createQuery()
                                           .forRevisionsOfEntity(Permission.class, false, true)
                                           .add(AuditEntity.id()
                                                           .eq(entityId));
        return AuditQueryUtils.getAuditQueryResults(auditQuery, Permission.class)
                              .stream()
                              // Turn into the CustomerHistory Domain Object:
                              .map(auditQueryResult -> new PermissionHistory(auditQueryResult.getEntity(),
                                                                             auditQueryResult.getRevision()
                                                                                             .getRevisionNumber(),
                                                                             auditQueryResult.getType()))
                              // And collect the Results:
                              .collect(Collectors.toList());
    }
}
