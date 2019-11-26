package com.takeaway.authorization.runtime.persistence;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * User: StMinko Date: 14.10.2019 Time: 10:08
 *
 * <p>
 */
@MappedSuperclass
public abstract class AuditedUUIDEntity extends AuditedEntity<UUID>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public String toString()
  {
    return "AuditedUUIDEntity{" + super.toString() + '}';
  }

  // =================  protected/package local  Methods ===================

  protected void onPrePersist()
  {
    if (isNew())
    {
      setId(UUID.randomUUID());
    }
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
