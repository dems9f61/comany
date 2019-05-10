package com.takeaway.employeeservice.department.control;

import lombok.NonNull;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:55
 * <p/>
 */
public class DepartmentServiceException extends Exception
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    DepartmentServiceException(@NonNull String message)
    {
        super(message);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
