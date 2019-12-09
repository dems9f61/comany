package com.takeaway.authorization.runtime.security.entity;

import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 18.11.2019 Time: 12:37
 *
 * <p>
 */
@Component
public class CustomUserDetailsEnhancer
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public CustomUserDetails enhance(CustomUserDetails detailsToEnhance)
    {
        return detailsToEnhance;
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
