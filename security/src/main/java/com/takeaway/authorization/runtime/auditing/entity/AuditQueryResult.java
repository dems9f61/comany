package com.takeaway.authorization.runtime.auditing.entity;

import com.takeaway.authorization.runtime.persistence.AuditedEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.RevisionType;

import java.io.Serializable;

/**
 * User: StMinko Date: 17.10.2019 Time: 16:19
 *
 * <p>
 */
@Getter
@RequiredArgsConstructor
public class AuditQueryResult<ID extends Serializable, ENTITY extends AuditedEntity<ID>>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final ENTITY entity;

  private final CustomRevisionEntity revision;

  private final RevisionType type;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
