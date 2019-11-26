package com.takeaway.authorization.runtime.security.control;

import java.util.UUID;

/**
 * User: StMinko Date: 11.10.2019 Time: 17:07
 *
 * <p>
 */
public class EntitySecurityHolder
{
  // =========================== Class Variables ===========================

  private static SecurityProvider instance;

  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public static void set(SecurityProvider holder)
  {
    instance = holder;
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================

  public interface SecurityProvider
  {
      String getActingUser();
  }

  static class DummyHolder implements SecurityProvider
  {
    @Override
    public String getActingUser()
    {
        return UUID.randomUUID()
                   .toString();
    }
  }

  public static SecurityProvider get()
  {
    if (instance == null)
    {
      instance = new DummyHolder();
    }
    return instance;
  }

  // ============================  End of class  ===========================
}
