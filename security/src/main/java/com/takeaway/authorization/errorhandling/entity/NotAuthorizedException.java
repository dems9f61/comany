package com.takeaway.authorization.errorhandling.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko Date: 08.11.2019 Time: 17:10
 *
 * <p>
 */
public class NotAuthorizedException extends ApiException
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public NotAuthorizedException(String errorMessage)
    {
        super(errorMessage, HttpStatus.FORBIDDEN);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
