package com.takeaway.employeeservice.employee.entity;

import lombok.Data;

/**
 * User: StMinko
 * Date: 23.08.2019
 * Time: 16:19
 * <p/>
 */
@Data
public class EmployeeMessage
{
    // =========================== Class Variables ===========================

    public enum EventType
    {
        EMPLOYEE_CREATED,

        EMPLOYEE_UPDATED,

        EMPLOYEE_DELETED
    }

    // =============================  Variables  =============================

    private EventType eventType;

    private Employee employee;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
