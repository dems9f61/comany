package com.takeaway.authorization.runtime.auditing.control;

import com.takeaway.authorization.runtime.auditing.entity.AuditQueryResult;
import com.takeaway.authorization.runtime.persistence.AuditedEntity;
import org.hibernate.envers.query.AuditQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: StMinko Date: 17.10.2019 Time: 16:31
 *
 * <p>
 */
public final class AuditQueryUtils
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  private AuditQueryUtils()
  {
    throw new AssertionError("Not meant to be instantiated!");
  }
  // ===========================  public  Methods  =========================

  public static <ID extends Serializable, ENTITY extends AuditedEntity<ID>> List<AuditQueryResult<ID, ENTITY>> getAuditQueryResults(AuditQuery query, Class<ENTITY> targetType)
  {
    List<?> results = query.getResultList();

    if (results == null)
    {
      return new ArrayList<>();
    }

    // The AuditReader returns a List of Object[], where the indices are:
    //
    // 0 - The queried entity
    // 1 - The revision entity
    // 2 - The Revision Type
    //
    // We cast it into something useful for a safe access:
    return results.stream()
        // Only use Object[] results:
        .filter(x -> x instanceof Object[])
        // Then convert to Object[]:
        .map(x -> (Object[]) x)
        // Transform into the AuditQueryResult:
        .map(x -> AuditQueryResultUtils.getAuditQueryResult(x, targetType))
        // And collect the Results into a List:
        .collect(Collectors.toList());
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
