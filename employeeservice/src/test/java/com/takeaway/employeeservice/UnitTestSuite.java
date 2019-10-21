package com.takeaway.employeeservice;

import com.takeaway.employeeservice.department.control.DepartmentParameterTestFactory;
import com.takeaway.employeeservice.department.entity.DepartmentRequestTestFactory;
import com.takeaway.employeeservice.department.entity.DepartmentTestFactory;
import com.takeaway.employeeservice.employee.control.EmployeeParameterTestFactory;
import com.takeaway.employeeservice.employee.entity.CreateEmployeeRequestTestFactory;
import com.takeaway.employeeservice.employee.entity.EmployeeTestFactory;
import com.takeaway.employeeservice.employee.entity.UpdateEmployeeRequestTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 14:41
 * <p/>
 */
@ExtendWith(MockitoExtension.class)
public abstract class UnitTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    protected DepartmentParameterTestFactory departmentParameterTestFactory;

    protected DepartmentTestFactory departmentTestFactory;

    protected DepartmentRequestTestFactory createDepartmentRequestTestFactory;

    protected EmployeeTestFactory employeeTestFactory;

    protected EmployeeParameterTestFactory employeeParameterTestFactory;

    protected CreateEmployeeRequestTestFactory createEmployeeRequestTestFactory;

    protected UpdateEmployeeRequestTestFactory updateEmployeeRequestTestFactory;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @BeforeEach
    public void initFactories()
    {
        departmentParameterTestFactory = new DepartmentParameterTestFactory();
        departmentTestFactory = new DepartmentTestFactory();
        createDepartmentRequestTestFactory = new DepartmentRequestTestFactory();
        employeeTestFactory = new EmployeeTestFactory();
        employeeParameterTestFactory = new EmployeeParameterTestFactory();
        createEmployeeRequestTestFactory = new CreateEmployeeRequestTestFactory();
        updateEmployeeRequestTestFactory = new UpdateEmployeeRequestTestFactory();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
