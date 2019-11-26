package com.takeaway.authorization.runtime.persistence;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * User: StMinko Date: 11.10.2019 Time: 17:10
 *
 * <p>
 */
@Audited
@AuditOverride(forClass = AbstractEntity.class)
@MappedSuperclass
public abstract class AuditedEntity<ID extends Serializable> extends AbstractEntity<ID> {
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
