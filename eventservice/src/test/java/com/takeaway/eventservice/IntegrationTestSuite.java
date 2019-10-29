package com.takeaway.eventservice;

import com.takeaway.eventservice.employee.messaging.EmployeeEventTestFactory;
import com.takeaway.eventservice.employee.messaging.EmployeeMessageTestFactory;
import com.takeaway.eventservice.employee.messaging.boundary.EmployeeMessageReceiver;
import com.takeaway.eventservice.employee.messaging.dto.EmployeeTestFactory;
import com.takeaway.eventservice.employee.messaging.entity.Employee;
import com.takeaway.eventservice.employee.messaging.entity.EmployeeMessage;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * User: StMinko Date: 20.03.2019 Time: 18:31
 *
 * <p>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
    classes = {EventServiceApplication.class})
@TestPropertySource(properties = {"config.asyncEnabled=false"})
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
  protected DatabaseCleaner databaseCleaner;

  @Autowired
  private EmployeeMessageReceiver employeeMessageReceiver;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public void receiveRandomMessageFor(UUID uuid)
  {
    receiveRandomMessageFor(uuid, 0);
  }

  public void receiveRandomMessageFor(int count)
  {
    receiveRandomMessageFor(null, 0);
  }

  public void receiveRandomMessageFor(UUID uuid, int count)
  {
    List<Employee> employees = employeeTestFactory.createManyDefault(count <= 0 ? RandomUtils.nextInt(30, 100) : count);
    employees.forEach(employee -> {
          employee.setId(uuid);
          EmployeeMessage employeeMessage = employeeMessageTestFactory.builder().employee(employee).create();
          employeeMessageReceiver.receiveEmployeeMessage(employeeMessage);
        });
  }
  // =================  protected/package local  Methods ===================

  @AfterEach
  void tearDown()
  {
    databaseCleaner.cleanDatabases();
  }

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
