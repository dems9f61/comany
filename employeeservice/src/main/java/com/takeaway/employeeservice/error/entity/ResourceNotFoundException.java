package com.takeaway.employeeservice.error.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:26
 * <p/>
 */
public class ResourceNotFoundException extends ApiException
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public ResourceNotFoundException(String message)
    {
        super(message, HttpStatus.NOT_FOUND);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
