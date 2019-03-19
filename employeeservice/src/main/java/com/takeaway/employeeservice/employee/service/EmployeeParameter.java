package com.takeaway.employeeservice.employee.service;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:41
 * <p/>
 */
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class EmployeeParameter
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final String emailAddress;

    private final String firstName;

    private final String lastName;

    private final LocalDate birthday;

    private final String departmentName;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
