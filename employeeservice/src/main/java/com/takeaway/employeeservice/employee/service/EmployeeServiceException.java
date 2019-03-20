package com.takeaway.employeeservice.employee.service;

import lombok.Getter;
import lombok.NonNull;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:56
 * <p/>
 */
public class EmployeeServiceException extends Exception
{
    // =========================== Class Variables ===========================

    public enum Reason
    {
        NOT_FOUND,
        INVALID_REQUEST,
        GENERIC
    }

    // =============================  Variables  =============================

    @Getter
    private final Reason reason;

    // ============================  Constructors  ===========================

    EmployeeServiceException(@NonNull Reason reason, @NonNull String message)
    {
        super(message);
        this.reason = reason;
    }

    EmployeeServiceException(@NonNull Exception exception)
    {
        super(exception);
        this.reason = Reason.GENERIC;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
