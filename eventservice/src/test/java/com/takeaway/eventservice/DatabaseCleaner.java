package com.takeaway.eventservice;

import com.takeaway.eventservice.employee.crud_management.control.EmployeeEventRepositoryTestHelper;
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

    private final EmployeeEventRepositoryTestHelper employeeEventRepositoryTestHelper;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void cleanDatabases()
    {
        LOGGER.info("Cleaning up the test database");
        employeeEventRepositoryTestHelper.cleanDatabase();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
