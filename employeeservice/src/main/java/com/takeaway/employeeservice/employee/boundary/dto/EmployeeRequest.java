package com.takeaway.employeeservice.employee.boundary.dto;

import com.takeaway.employeeservice.employee.control.EmployeeParameter;

import java.util.Date;

/**
 * User: StMinko
 * Date: 10.04.2019
 * Time: 23:09
 * <p/>
 */
public interface EmployeeRequest
{
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
}
