package com.takeaway.authorization.integrationsupport.entity;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * User: StMinko Date: 19.10.2019 Time: 16:06
 *
 * <p>
 */
@MappedSuperclass
public abstract class UUIDEntity extends AbstractEntity<UUID>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public String toString()
  {
    return "UUIDEntity{" + super.toString() + "}";
  }

  // =================  protected/package local  Methods ===================

  @Override
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
