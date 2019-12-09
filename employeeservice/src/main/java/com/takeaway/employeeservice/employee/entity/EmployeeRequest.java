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

    // Regex emailPattern to valid email address:
    // at the beginning only a-z, A-Z, 0-9 -_. are valid except for @
    // after an @ a-z, A-Z, 0-9 - is valid except for further @
    // after the last dot (.) only the defined characters [a-zA-Z] are valid with a minimum of 2
    // Does not support special chars any longer, besides -_.
    String EMAIL_REGEX = "^[a-zA-Z0-9\\-_.]+@[a-zA-Z0-9\\-.]+\\.[a-zA-Z]{2,}$";

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
