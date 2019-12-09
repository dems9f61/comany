package com.takeaway.eventservice;

import com.takeaway.eventservice.employee.messaging.EmployeeEventTestFactory;
import com.takeaway.eventservice.employee.messaging.EmployeeMessageTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * User: StMinko Date: 21.03.2019 Time: 09:41
 *
 * <p>
 */
@ExtendWith(MockitoExtension.class)
public abstract class UnitTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    protected EmployeeEventTestFactory employeeEventTestFactory;

    protected EmployeeMessageTestFactory employeeMessageTestFactory;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @BeforeEach
    public void initFactories()
    {
        employeeEventTestFactory = new EmployeeEventTestFactory();
        employeeMessageTestFactory = new EmployeeMessageTestFactory();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
