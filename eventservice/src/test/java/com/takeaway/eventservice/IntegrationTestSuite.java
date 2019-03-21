package com.takeaway.eventservice;

import com.takeaway.eventservice.messaging.EmployeeEventTestFactory;
import com.takeaway.eventservice.messaging.EmployeeMessage;
import com.takeaway.eventservice.messaging.EmployeeMessageReceiver;
import com.takeaway.eventservice.messaging.EmployeeMessageTestFactory;
import com.takeaway.eventservice.messaging.dto.Employee;
import com.takeaway.eventservice.messaging.dto.EmployeeTestFactory;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:31
 * <p/>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = { EventServiceApplication.class })
@TestPropertySource(properties = { "config.asyncEnabled=false" })
public abstract class IntegrationTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Autowired
    protected EmployeeTestFactory employeeTestFactory;

    @Autowired
    protected EmployeeEventTestFactory employeeEventTestFactory;

    @Autowired
    protected EmployeeMessageTestFactory employeeMessageTestFactory;

    @Autowired
    private EmployeeMessageReceiver employeeMessageReceiver;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void receiveRandomMessageFor(String uuid)
    {
        receiveRandomMessageFor(uuid, 0);
    }

    public void receiveRandomMessageFor(int count)
    {
        receiveRandomMessageFor(null, 0);
    }

    public void receiveRandomMessageFor(String uuid, int count)
    {
        List<Employee> employees = employeeTestFactory.createManyDefault(count <= 0 ? RandomUtils.nextInt(30, 100) : count);
        employees.forEach(employee -> {
            if (!StringUtils.isBlank(uuid))
            {
                employee.setUuid(uuid);
            }
            EmployeeMessage employeeMessage = employeeMessageTestFactory.builder()
                                                                        .employee(employee)
                                                                        .create();
            employeeMessageReceiver.receiveEmployeeMessage(employeeMessage);
        });
    }
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
