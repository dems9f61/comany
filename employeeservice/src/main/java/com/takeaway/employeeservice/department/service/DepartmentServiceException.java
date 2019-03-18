package com.takeaway.employeeservice.department.service;

import lombok.Getter;
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

    public enum Raison
    {
        ALREADY_EXISTING_DEPARTMENT,
        AMBIGUOUS_RESULTS,
        INVALID_REQUEST
    }

    // =============================  Variables  =============================

    @Getter
    private final Raison raison;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    DepartmentServiceException(@NonNull DepartmentServiceException.Raison raison, @NonNull String message)
    {
        super(message);
        this.raison = raison;
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
