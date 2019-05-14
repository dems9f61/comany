package com.takeaway.employeeservice.error.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:17
 * <p/>
 */
public class ApiException extends RuntimeException
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Getter
    private final HttpStatus httpStatus;

    // ============================  Constructors  ===========================

    public ApiException(String message, HttpStatus httpStatus)
    {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, HttpStatus httpStatus, Throwable cause)
    {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
