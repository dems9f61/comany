package com.takeaway.authorization.runtime.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * User: StMinko Date: 30.10.2019 Time: 17:27
 * <p>
 */
@Data
public final class UserInformation implements Serializable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final UUID userId;

    private final Collection<? super GrantedAuthority> permissions;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
