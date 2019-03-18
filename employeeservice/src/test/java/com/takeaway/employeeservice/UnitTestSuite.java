package com.takeaway.employeeservice;

import com.takeaway.employeeservice.department.service.DepartmentCreationParameterTestFactory;
import com.takeaway.employeeservice.department.service.DepartmentTestFactory;
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

    protected DepartmentCreationParameterTestFactory departmentCreationParameterTestFactory;

    protected DepartmentTestFactory departmentTestFactory;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @BeforeEach
    public void initFactories()
    {
        departmentCreationParameterTestFactory = new DepartmentCreationParameterTestFactory();
        departmentTestFactory = new DepartmentTestFactory();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
