package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.IntegrationTestSuite;
import com.takeaway.employeeservice.department.control.DepartmentParameter;
import com.takeaway.employeeservice.department.control.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.employee.entity.Employee;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.*;
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
    @Autowired
    private DepartmentServiceCapable departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Nested
    @DisplayName("when new")
    class WhenNew
    {
        @Test
        @DisplayName("Creating an employee with valid parameters succeeds")
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
            assertThat(birthday).isNotNull()
                                .isEqualTo(employeeParameter.getBirthday());
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
            if (!foundEmployeeOpt.isPresent())
            {
                fail("Fail to retrieve the  employee");
            }
            else
            {
                Employee foundEmployee = foundEmployeeOpt.get();
                assertThat(foundEmployee.getUuid()).isEqualTo(employee.getUuid());
                assertThat(foundEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                assertThat(foundEmployee.getFullName()).isEqualTo(employee.getFullName());
                assertThat(foundEmployee.getDepartment()
                                        .getId()).isEqualTo(employee.getDepartment()
                                                                    .getId());
            }

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
            verify(employeeEventPublisher).employeeDeleted(assertArg(publishedEmployee -> assertThat(publishedEmployee.getUuid()).isEqualTo(uuid)));
        }
    }

    @Nested
    @DisplayName("when update")
    class WhenUpdate
    {
        @Test
        @DisplayName("Updating all employee fields with valid parameters succeeds")
        void givenValidRequestParams_whenUpdate_thenSucceed() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);

            EmployeeParameter updateParameters = employeeParameterTestFactory.createDefault();
            DepartmentParameter newDepartmentParameter = departmentParameterTestFactory.builder()
                                                                                       .departmentName(updateParameters.getDepartmentName())
                                                                                       .create();
            departmentService.create(newDepartmentParameter);

            // Act
            employeeService.update(employee.getUuid(), updateParameters);

            // Assert
            Optional<Employee> updated = employeeService.findByUuid(employee.getUuid());
            if (!updated.isPresent())
            {
                fail("Fail to retrieve the updated employee");
            }
            else
            {
                Employee updatedEmployee = updated.get();
                assertThat(updatedEmployee.getEmailAddress()).isEqualTo(updateParameters.getEmailAddress());
                assertThat(updatedEmployee.getFullName()
                                          .getFirstName()).isEqualTo(updateParameters.getFirstName());
                assertThat(updatedEmployee.getFullName()
                                          .getLastName()).isEqualTo(updateParameters.getLastName());
                assertThat(updatedEmployee.getBirthday()).isEqualTo(updateParameters.getBirthday());
                assertThat(updatedEmployee.getDepartment()
                                          .getDepartmentName()).isEqualTo(updateParameters.getDepartmentName());
                verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getUuid()).isEqualTo(
                        updatedEmployee.getUuid())));
            }
        }

        @Test
        @DisplayName("Updating an employee birthday succeeds without affecting other values")
        void givenValidBirthday_whenUpdate_thenUpdateOnlyBirthDay() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);

            LocalDate localDate = employeeParameterTestFactory.builder()
                                                              .generateRandomDate();
            java.sql.Date newBirthDay = java.sql.Date.valueOf(localDate);
            EmployeeParameter updateParameters = employeeParameterTestFactory.builder()
                                                                             .emailAddress(null)
                                                                             .departmentName(null)
                                                                             .firstName(null)
                                                                             .lastName(null)
                                                                             .birthday(newBirthDay)
                                                                             .create();

            // Act
            employeeService.update(employee.getUuid(), updateParameters);

            // Assert
            Optional<Employee> updated = employeeService.findByUuid(employee.getUuid());
            if (!updated.isPresent())
            {
                fail("Fail to retrieve the updated employee");
            }
            else
            {
                Employee updatedEmployee = updated.get();
                assertThat(updatedEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                assertThat(updatedEmployee.getFullName()
                                          .getFirstName()).isEqualTo(employee.getFullName().getFirstName());
                assertThat(updatedEmployee.getFullName()
                                          .getLastName()).isEqualTo(employee.getFullName().getLastName());
                assertThat(updatedEmployee.getDepartment()
                                          .getDepartmentName()).isEqualTo(employee.getDepartment().getDepartmentName());
                assertThat(updatedEmployee.getBirthday()).isEqualTo(newBirthDay);
                verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getUuid()).isEqualTo(
                        updatedEmployee.getUuid())));
            }
        }

        @Test
        @DisplayName("Updating an employee full name succeeds without affecting other values")
        void givenValidFullName_whenUpdate_thenUpdateOnlyFullName() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);

            String expectedFirstName = RandomStringUtils.randomAlphabetic(23);
            String expectedLastName = RandomStringUtils.randomAlphabetic(23);

            EmployeeParameter updateParameters = employeeParameterTestFactory.builder()
                                                                             .emailAddress(null)
                                                                             .departmentName(null)
                                                                             .firstName(expectedFirstName)
                                                                             .lastName(expectedLastName)
                                                                             .birthday(null)
                                                                             .create();

            // Act
            employeeService.update(employee.getUuid(), updateParameters);

            // Assert
            Optional<Employee> updated = employeeService.findByUuid(employee.getUuid());
            if (!updated.isPresent())
            {
                fail("Fail to retrieve the updated employee");
            }
            else
            {
                Employee updatedEmployee = updated.get();
                assertThat(updatedEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                assertThat(updatedEmployee.getFullName()
                                          .getFirstName()).isEqualTo(expectedFirstName);
                assertThat(updatedEmployee.getFullName()
                                          .getLastName()).isEqualTo(expectedLastName);
                assertThat(updatedEmployee.getDepartment()
                                          .getDepartmentName()).isEqualTo(employee.getDepartment()
                                                                                  .getDepartmentName());
                assertThat(updatedEmployee.getBirthday()).isEqualTo(employee.getBirthday());
                verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getUuid()).isEqualTo(
                        updatedEmployee.getUuid())));
            }
        }

        @Test
        @DisplayName("Updating an employee email succeeds without affecting other values")
        void givenValidEmail_whenUpdate_thenUpdateOnlyEmail() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);

            String expectedEmail =employeeParameterTestFactory.builder().generateRandomEmail();

            EmployeeParameter updateParameters = employeeParameterTestFactory.builder()
                                                                             .emailAddress(expectedEmail)
                                                                             .departmentName(null)
                                                                             .firstName(null)
                                                                             .lastName(null)
                                                                             .birthday(null)
                                                                             .create();

            // Act
            employeeService.update(employee.getUuid(), updateParameters);

            // Assert
            Optional<Employee> updated = employeeService.findByUuid(employee.getUuid());
            if (!updated.isPresent())
            {
                fail("Fail to retrieve the updated employee");
            }
            else
            {
                Employee updatedEmployee = updated.get();
                assertThat(updatedEmployee.getEmailAddress()).isEqualTo(expectedEmail);
                assertThat(updatedEmployee.getFullName()
                                          .getFirstName()).isEqualTo(employee.getFullName().getFirstName());
                assertThat(updatedEmployee.getFullName()
                                          .getLastName()).isEqualTo(employee.getFullName().getLastName());
                assertThat(updatedEmployee.getDepartment()
                                          .getDepartmentName()).isEqualTo(employee.getDepartment()
                                                                                  .getDepartmentName());
                assertThat(updatedEmployee.getBirthday()).isEqualTo(employee.getBirthday());
                verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getUuid()).isEqualTo(
                        updatedEmployee.getUuid())));
            }
        }

        @Test
        @DisplayName("Updating an employee with wrong uuid fails")
        void givenUnknownUuid_whenUpdate_thenThrowException()
        {
            // Arrange
            EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.update(UUID.randomUUID()
                                                                                                                  .toString(), employeeParameter));
        }

        @Test
        @DisplayName("Updating an employee with unknown department fails")
        void givenUnknownDepartment_whenUpdate_thenThrowException() throws Exception
        {
            // Arrange
            DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
            departmentService.create(departmentParameter);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.builder()
                                                                              .departmentName(departmentParameter.getDepartmentName())
                                                                              .create();
            Employee employee = employeeService.create(employeeParameter);
            EmployeeParameter updateParameters = employeeParameterTestFactory.createDefault();

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.update(employee.getUuid(), updateParameters));
        }
    }
}