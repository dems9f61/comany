package com.takeaway.employeeservice.error.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:19
 * <p/>
 */
public class InternalServerErrorException extends ApiException
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public InternalServerErrorException(String message)
    {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
