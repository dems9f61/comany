package com.takeaway.authentication.integrationsupport.control;

import java.util.UUID;

/**
 * User: StMinko
 * Date: 11.10.2019
 * Time: 17:07
 * <p/>
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
        UUID getUser();
    }

    static class DummyHolder implements SecurityProvider
    {
        @Override
        public UUID getUser()
        {
            return UUID.randomUUID();
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
