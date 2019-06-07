package com.takeaway.employeeservice.aop;

import com.takeaway.employeeservice.department.control.DepartmentRepositoryTestHelper;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.employee.control.EmployeeRepositoryTestHelper;
import com.takeaway.employeeservice.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * User: StMinko
 * Date: 07.06.2019
 * Time: 12:07
 * <p/>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DatabaseEntryTrackingAspect
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final DepartmentRepositoryTestHelper departmentRepositoryTestHelper;

    private final EmployeeRepositoryTestHelper employeeRepositoryTestHelper;

    private final Set<Long> savedDepartments = new HashSet<>();

    private final Set<String> savedEmployees = new HashSet<>();

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void cleanDatabase()
    {
        LOGGER.info("Cleaning up the test database");
        employeeRepositoryTestHelper.cleanDatabase(savedEmployees);
        departmentRepositoryTestHelper.cleanDatabase(savedDepartments);
    }

    // =================  protected/package local  Methods ===================

    @AfterReturning(value = "com.takeaway.employeeservice.aop.DatabaseEntryJoinPointConfig.saveEmployeeExecution()",
            returning = "savedEmployee")
    void afterCompletingEmployeeSave(Employee savedEmployee)
    {
        String employeeUuid = savedEmployee.getUuid();
        LOGGER.info("Tracking test employee '{}'", employeeUuid);
        savedEmployees.add(employeeUuid);
    }

    @AfterReturning(value = "com.takeaway.employeeservice.aop.DatabaseEntryJoinPointConfig.saveDepartmentExecution()",
            returning = "savedDepartment")
    void afterCompletingDepartmentSave(Department savedDepartment)
    {
        long departmentId = savedDepartment.getId();
        LOGGER.info("Tracking test department '{}'", departmentId);
        savedDepartments.add(departmentId);
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
