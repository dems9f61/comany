package com.takeaway.authorization.runtime.auditing.control;

import com.takeaway.authorization.runtime.auditing.entity.AuditQueryResult;
import com.takeaway.authorization.runtime.auditing.entity.CustomRevisionEntity;
import com.takeaway.authorization.runtime.persistence.AuditedEntity;
import org.hibernate.envers.RevisionType;

import java.io.Serializable;

/**
 * User: StMinko Date: 17.10.2019 Time: 16:33
 *
 * <p>
 */
class AuditQueryResultUtils
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    private AuditQueryResultUtils()
    {
        throw new AssertionError("Not meant to be instantiated");
    }

    // ===========================  public  Methods  =========================

    static <ID extends Serializable, ENTITY extends AuditedEntity<ID>> AuditQueryResult<ID, ENTITY> getAuditQueryResult(Object[] item, Class<ENTITY> type)
    {
        // Early exit, if no item given:
        if (item == null)
        {
            return null;
        }

        // Early exit, if there is not enough data:
        if (item.length < 3)
        {
            return null;
        }

        // Cast item[0] to the Entity:
        ENTITY entity = null;
        if (type.isInstance(item[0]))
        {
            entity = type.cast(item[0]);
        }

        // Then get the Revision Entity:
        CustomRevisionEntity revision = null;
        if (item[1] instanceof CustomRevisionEntity)
        {
            revision = (CustomRevisionEntity) item[1];
        }

        // Then get the Revision Type:
        RevisionType revisionType = null;
        if (item[2] instanceof RevisionType)
        {
            revisionType = (RevisionType) item[2];
        }

        // Build the Query Result:
        return new AuditQueryResult<>(entity, revision, revisionType);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
