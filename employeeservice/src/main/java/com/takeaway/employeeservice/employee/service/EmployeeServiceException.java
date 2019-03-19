package com.takeaway.employeeservice.employee.service;

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
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    EmployeeServiceException(@NonNull String message)
    {
        super(message);
    }

    EmployeeServiceException(Exception exception)
    {
        super(exception);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
