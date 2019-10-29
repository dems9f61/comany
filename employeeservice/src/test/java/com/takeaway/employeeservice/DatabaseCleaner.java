package com.takeaway.employeeservice;

import com.takeaway.employeeservice.department.control.DepartmentRepositoryTestHelper;
import com.takeaway.employeeservice.employee.control.EmployeeRepositoryTestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 07.06.2019 Time: 12:07
 *
 * <p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleaner
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final DepartmentRepositoryTestHelper departmentRepositoryTestHelper;

  private final EmployeeRepositoryTestHelper employeeRepositoryTestHelper;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public void cleanDatabases()
  {
    LOGGER.info("Cleaning up the test database");
    employeeRepositoryTestHelper.cleanDatabase();
    departmentRepositoryTestHelper.cleanDatabase();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
