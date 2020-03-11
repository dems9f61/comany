package com.takeaway.authorization.runtime.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;

/**
 * User: StMinko Date: 15.01.2020 Time: 10:47
 * <p>
 */
class PageModule extends SimpleModule
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public PageModule()
    {
        addDeserializer(Page.class, new PageDeserializer());
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
