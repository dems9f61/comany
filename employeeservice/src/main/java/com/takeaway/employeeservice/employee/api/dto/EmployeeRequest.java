package com.takeaway.employeeservice.employee.api.dto;

import com.takeaway.employeeservice.employee.service.EmployeeParameter;

import java.util.Date;

/**
 * User: StMinko
 * Date: 10.04.2019
 * Time: 23:09
 * <p/>
 */
public interface EmployeeRequest
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

   String getEmailAddress();

   String getFirstName();

   String getLastName();

   Date getBirthday();

   String getDepartmentName();

    default EmployeeParameter toEmployerParameter()
    {
        return EmployeeParameter.builder()
                                .emailAddress(getEmailAddress())
                                .birthday(getBirthday())
                                .firstName(getFirstName())
                                .lastName(getLastName())
                                .departmentName(getDepartmentName())
                                .build();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
