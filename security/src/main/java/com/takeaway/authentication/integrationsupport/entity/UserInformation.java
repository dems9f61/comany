package com.takeaway.authentication.integrationsupport.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 30.10.2019
 * Time: 17:27
 * <p/>
 */
@Data
public final class UserInformation
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final UUID                                 userId;

    private final Collection<? super GrantedAuthority> permissions;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
