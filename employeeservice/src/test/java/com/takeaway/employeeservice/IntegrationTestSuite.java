package com.takeaway.employeeservice;

import com.takeaway.employeeservice.department.api.dto.CreateDepartmentRequestTestFactory;
import com.takeaway.employeeservice.department.service.DepartmentParameterTestFactory;
import com.takeaway.employeeservice.employee.service.EmployeeParameterTestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 12:57
 * <p/>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = { EmployeeServiceApplication.class })
public abstract class IntegrationTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Autowired
    protected DepartmentParameterTestFactory departmentParameterTestFactory;

    @Autowired
    protected EmployeeParameterTestFactory employeeParameterTestFactory;

    @Autowired
    protected CreateDepartmentRequestTestFactory createDepartmentRequestTestFactory;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
