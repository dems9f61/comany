package com.takeaway.employeeservice.employee.entity;

import com.takeaway.employeeservice.employee.control.EmployeeParameter;

import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 10.04.2019 Time: 23:09
 *
 * <p>
 */
public interface EmployeeRequest
{
  String getEmailAddress();

  String getFirstName();

  String getLastName();

  ZonedDateTime getBirthday();

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
