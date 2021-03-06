package com.takeaway.employeeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.employeeservice.department.control.DepartmentParameterTestFactory;
import com.takeaway.employeeservice.department.entity.DepartmentRequestTestFactory;
import com.takeaway.employeeservice.employee.control.EmployeeEventPublisherCapable;
import com.takeaway.employeeservice.employee.control.EmployeeParameterTestFactory;
import com.takeaway.employeeservice.employee.entity.EmployeeRequestTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * User: StMinko Date: 18.03.2019 Time: 12:57
 *
 * <p>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = {EmployeeServiceApplication.class})
@ActiveProfiles("INTEGRATION")
public abstract class IntegrationTestSuite
{
    // =========================== Class Variables ===========================

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                           MediaType.APPLICATION_JSON.getSubtype(),
                                                                           StandardCharsets.UTF_8);

    // =============================  Variables  =============================

    @Autowired
    protected DepartmentParameterTestFactory departmentParameterTestFactory;

    @Autowired
    protected EmployeeParameterTestFactory employeeParameterTestFactory;

    @Autowired
    protected DepartmentRequestTestFactory departmentRequestTestFactory;

    @Autowired
    protected EmployeeRequestTestFactory employeeRequestTestFactory;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @SpyBean
    protected EmployeeEventPublisherCapable employeeEventPublisher;

    @Autowired
    protected ObjectMapper objectMapper;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @BeforeAll
    static void setUpForAll()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @BeforeEach
    void setUp()
    {
        doNothing().when(employeeEventPublisher).employeeCreated(any());
        doNothing().when(employeeEventPublisher).employeeDeleted(any());
        doNothing().when(employeeEventPublisher).employeeUpdated(any());
    }

    @AfterEach
    void tearDown()
    {
        databaseCleaner.cleanDatabases();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
