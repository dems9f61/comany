package com.takeaway.eventservice;

import com.takeaway.eventservice.messaging.EmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import com.takeaway.eventservice.messaging.dto.EmployeeTestFactory;
import com.takeaway.eventservice.messaging.dto.EventType;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Random;

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
    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    protected EmployeeTestFactory employeeTestFactory;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void publishRandomEventsFor(String uuid)
    {
        List<Employee> employees = employeeTestFactory.createManyDefault(RandomUtils.nextInt(30, 100));
        EventType[] values = EventType.values();
        employees.forEach(employee -> {
            Random random = new Random();
            EventType value = values[random.nextInt(values.length)];
            employee.setUuid(uuid);
            eventPublisher.publishEvent(new EmployeeEvent(employee, value));
        });
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
