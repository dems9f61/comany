package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.IntegrationTestSuite;
import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentParameter;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 11:35
 * <p/>
 */
@DisplayName("Integration tests for the employee service")
class EmployeeServiceIntegrationTest extends IntegrationTestSuite
{
    @SpyBean
    private EmployeeEventPublisher employeeEventPublisher;

    @Autowired
    private DepartmentServiceCapable departmentService;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    void setUp()
    {
        doNothing().when(employeeEventPublisher)
                   .employeeCreated(any());
        doNothing().when(employeeEventPublisher)
                   .employeeDeleted(any());
        doNothing().when(employeeEventPublisher)
                   .employeeUpdated(any());
    }

    @Nested
    @DisplayName("when new")
    class WhenNew
    {
        @Test
        @DisplayName("Creating an employee with a valid parameter succeeds")
        void givenValidRequestParams_whenCreate_thenSucceed() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();

            // Act
            Employee employee = employeeService.create(employeeParameter);

            // Assert
            assertThat(employee).isNotNull();
            Employee.FullName fullName = employee.getFullName();
            assertThat(fullName.getFirstName()).isNotBlank()
                                               .isEqualTo(employeeParameter.getFirstName());
            assertThat(fullName.getLastName()).isNotBlank()
                                              .isEqualTo(employeeParameter.getLastName());
            assertThat(employee.getEmailAddress()).isNotBlank()
                                                  .isEqualTo(employeeParameter.getEmailAddress());
            Department department = employee.getDepartment();
            assertThat(department).isNotNull();
            assertThat(department.getDepartmentName()).isEqualTo(employeeParameter.getDepartmentName());
            Date birthday = employee.getBirthday();
            assertThat(birthday).isNotNull();
            LocalDate birthDateAsLocalDate = birthday.toInstant()
                                                     .atZone(ZoneId.systemDefault())
                                                     .toLocalDate();
            assertThat(birthDateAsLocalDate).isEqualTo(employeeParameter.getBirthday());
            verify(employeeEventPublisher).employeeCreated(employee);
        }

        @Test
        @DisplayName("Creating an employee with a wrong department name fails")
        void givenWrongDepartmentName_whenCreate_thenThrowException()
        {
            //Arrange
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(RandomStringUtils.randomAlphabetic(4))
                                                                              .create();
            // Act/ Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.create(employeeParameter));
        }

        @Test
        @DisplayName("Creating two employees with the same email fails")
        void givenEmailAddressToUseForTwoEmployees_whenCreate_thenThrowException() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            Department department = departmentService.create(departmentParameter);
            EmployeeParameter firstEmployeeParameter = employeeParameterTestFactory.builder()
                                                                                   .departmentName(department.getDepartmentName())
                                                                                   .create();
            employeeService.create(firstEmployeeParameter);

            EmployeeParameter secondEmployeeParameter = employeeParameterTestFactory.builder()
                                                                                    .departmentName(department.getDepartmentName())
                                                                                    .emailAddress(firstEmployeeParameter.getEmailAddress())
                                                                                    .create();

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.create(secondEmployeeParameter));
        }
    }

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding an employee with a wrong uuid fails")
        void givenUnknownUuid_whenFind_thenReturnNothing() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            List<EmployeeParameter> employeeParameters = new LinkedList<>();
            IntStream.range(0, RandomUtils.nextInt(20, 30))
                     .forEach(value -> employeeParameters.add(employeeParameterTestFactory.builder()
                                                                                          .departmentName(departmentParameter.getDepartmentName())
                                                                                          .create()));

            for (EmployeeParameter employeeParameter : employeeParameters)
            {
                employeeService.create(employeeParameter);
            }

            // Act
            Optional<Employee> foundEmployee = employeeService.findByUuid(UUID.randomUUID()
                                                                              .toString());
            // Assert
            assertThat(foundEmployee).isEmpty();
        }

        @Test
        @DisplayName("Finding an employee with a correct employee uuid returns the related employee")
        void givenEmployee_whenFind_thenReturnEmployee() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);

            // Act
            Optional<Employee> foundEmployeeOpt = employeeService.findByUuid(employee.getUuid());

            // Assert
            assertThat(foundEmployeeOpt).isPresent();
            Employee foundEmployee = foundEmployeeOpt.get();
            assertThat(foundEmployee.getUuid()).isEqualTo(employee.getUuid());
            assertThat(foundEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
            assertThat(foundEmployee.getFullName()).isEqualTo(employee.getFullName());
            assertThat(foundEmployee.getDepartment()
                                    .getId()).isEqualTo(employee.getDepartment()
                                                                .getId());
        }
    }

    @Nested
    @DisplayName("when delete")
    class WhenDelete
    {
        @Test
        @DisplayName("Deleting an employee with a wrong uuid fails")
        void givenUnknownUuid_whenFind_thenReturnNothing() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            List<EmployeeParameter> employeeParameters = new LinkedList<>();
            IntStream.range(0, RandomUtils.nextInt(30, 50))
                     .forEach(value -> employeeParameters.add(employeeParameterTestFactory.builder()
                                                                                          .departmentName(departmentParameter.getDepartmentName())
                                                                                          .create()));

            for (EmployeeParameter employeeParameter : employeeParameters)
            {
                employeeService.create(employeeParameter);
            }

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.deleteByUuid(UUID.randomUUID()
                                                                                                                        .toString()));
        }

        @Test
        @DisplayName("Deleting an employee with a correct employee uuid succeeds")
        void givenEmployee_whenDelete_thenSucceed() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);
            String uuid = employee.getUuid();

            // Act
            employeeService.deleteByUuid(uuid);

            // Assert
            assertThat(employeeService.findByUuid(uuid)).isEmpty();
        }
    }
}