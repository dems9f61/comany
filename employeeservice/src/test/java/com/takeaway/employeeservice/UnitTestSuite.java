package com.takeaway.employeeservice;

import com.takeaway.employeeservice.department.control.DepartmentParameterTestFactory;
import com.takeaway.employeeservice.department.entity.DepartmentRequestTestFactory;
import com.takeaway.employeeservice.department.entity.DepartmentTestFactory;
import com.takeaway.employeeservice.employee.control.EmployeeParameterTestFactory;
import com.takeaway.employeeservice.employee.entity.EmployeeRequestTestFactory;
import com.takeaway.employeeservice.employee.entity.EmployeeTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * User: StMinko Date: 18.03.2019 Time: 14:41
 *
 * <p>
 */
@ExtendWith(MockitoExtension.class)
public abstract class UnitTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    protected DepartmentParameterTestFactory departmentParameterTestFactory;

    protected DepartmentTestFactory departmentTestFactory;

    protected DepartmentRequestTestFactory departmentRequestTestFactory;

    protected EmployeeTestFactory employeeTestFactory;

    protected EmployeeParameterTestFactory employeeParameterTestFactory;

    protected EmployeeRequestTestFactory employeeRequestTestFactory;


    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @BeforeEach
    public void initFactories()
    {
        departmentParameterTestFactory = new DepartmentParameterTestFactory();
        departmentTestFactory = new DepartmentTestFactory();
        departmentRequestTestFactory = new DepartmentRequestTestFactory();
        employeeTestFactory = new EmployeeTestFactory();
        employeeParameterTestFactory = new EmployeeParameterTestFactory();
        employeeRequestTestFactory = new EmployeeRequestTestFactory();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
