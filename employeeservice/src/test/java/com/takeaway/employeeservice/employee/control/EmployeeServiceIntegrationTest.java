package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.IntegrationTestSuite;
import com.takeaway.employeeservice.department.control.DepartmentParameter;
import com.takeaway.employeeservice.department.control.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.employee.entity.UsableDateFormat;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.errorhandling.entity.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko Date: 19.03.2019 Time: 11:35
 *
 * <p>
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
    void givenValidRequestParams_whenCreate_thenSucceed()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();

      // Act
      Employee employee = employeeService.create(employeeParameter);

      // Assert
      assertThat(employee).isNotNull();
      Employee.FullName fullName = employee.getFullName();
      assertThat(fullName.getFirstName()).isNotBlank().isEqualTo(employeeParameter.getFirstName());
      assertThat(fullName.getLastName()).isNotBlank().isEqualTo(employeeParameter.getLastName());
      assertThat(employee.getEmailAddress()).isNotBlank().isEqualTo(employeeParameter.getEmailAddress());
      Department department = employee.getDepartment();
      assertThat(department).isNotNull();
      assertThat(department.getDepartmentName()).isEqualTo(employeeParameter.getDepartmentName());
      ZonedDateTime birthday = employee.getBirthday();
      assertThat(birthday).isNotNull().isEqualTo(employeeParameter.getBirthday());
      verify(employeeEventPublisher).employeeCreated(employee);
    }

    @Test
    @DisplayName("Creating an employee with a wrong department name fails")
    void givenWrongDepartmentName_whenCreate_thenThrowBadRequestException()
    {
      // Arrange
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(RandomStringUtils.randomAlphabetic(4)).create();
      // Act/ Assert
      assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeService.create(employeeParameter));
    }

    @Test
    @DisplayName("Creating two employees with the same email fails")
    void givenEmailAddressToUseForTwoEmployees_whenCreate_thenThrowBadRequestException()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      Department department = departmentService.create(departmentParameter);
      EmployeeParameter firstEmployeeParameter = employeeParameterTestFactory.builder().departmentName(department.getDepartmentName()).create();
      employeeService.create(firstEmployeeParameter);

      EmployeeParameter secondEmployeeParameter = employeeParameterTestFactory
              .builder()
              .departmentName(department.getDepartmentName())
              .emailAddress(firstEmployeeParameter.getEmailAddress())
              .create();

      // Act / Assert
      assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeService.create(secondEmployeeParameter));
    }
  }

  @Nested
  @DisplayName("when access")
  class WhenAccess
  {
    @Test
    @DisplayName("Finding an employee with a wrong uuid fails")
    void givenUnknownUuid_whenFind_thenThrowResourceNotFoundException()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      List<EmployeeParameter> employeeParameters = new LinkedList<>();
      IntStream.range(0, RandomUtils.nextInt(20, 30))
            .forEach(value -> employeeParameters.add(employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create()));

      for (EmployeeParameter employeeParameter : employeeParameters)
      {
        employeeService.create(employeeParameter);
      }
      UUID unknownId = UUID.randomUUID();

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeService.findById(unknownId));
    }

    @Test
    @DisplayName("Finding an employee with a correct employee uuid returns the related employee")
    void givenEmployee_whenFind_thenReturnEmployee()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);

      // Act
      Employee foundEmployee = employeeService.findById(employee.getId());

      // Assert
      assertThat(foundEmployee.getId()).isEqualTo(employee.getId());
      assertThat(foundEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
      assertThat(foundEmployee.getFullName()).isEqualTo(employee.getFullName());
      assertThat(foundEmployee.getDepartment().getId()).isEqualTo(employee.getDepartment().getId());
    }
  }

  @Nested
  @DisplayName("when delete")
  class WhenDelete
  {
    @Test
    @DisplayName("Deleting an employee with a wrong uuid fails")
    void givenUnknownUuid_whenFind_thenThrowResourceNotFoundException()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      List<EmployeeParameter> employeeParameters = new LinkedList<>();
      IntStream.range(0, RandomUtils.nextInt(30, 50))
            .forEach(value -> employeeParameters.add(employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create()));

      for (EmployeeParameter employeeParameter : employeeParameters)
      {
        employeeService.create(employeeParameter);
      }

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeService.deleteById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deleting an employee with a correct employee uuid succeeds")
    void givenEmployee_whenDelete_thenSucceed() 
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);
      UUID uuid = employee.getId();

      // Act
      employeeService.deleteById(uuid);

      // Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeService.findById(uuid));
      verify(employeeEventPublisher).employeeDeleted(assertArg(publishedEmployee -> assertThat(publishedEmployee.getId()).isEqualTo(uuid)));
    }
  }

  @Nested
  @DisplayName("when update")
  class WhenUpdate
  {
    @Test
    @DisplayName("Updating all employee fields with valid parameters succeeds")
    void givenValidRequestParams_whenUpdate_thenSucceed()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);

      EmployeeParameter updateParameters = employeeParameterTestFactory.createDefault();
      DepartmentParameter newDepartmentParameter = departmentParameterTestFactory.builder().departmentName(updateParameters.getDepartmentName()).create();
      departmentService.create(newDepartmentParameter);

      // Act
      employeeService.update(employee.getId(), updateParameters);

      // Assert
      Employee updated = employeeService.findById(employee.getId());
      assertThat(updated.getEmailAddress()).isEqualTo(updateParameters.getEmailAddress());
      assertThat(updated.getFullName().getFirstName()).isEqualTo(updateParameters.getFirstName());
      assertThat(updated.getFullName().getLastName()).isEqualTo(updateParameters.getLastName());
      assertThat(updated.getBirthday()).isEqualTo(updateParameters.getBirthday());
      assertThat(updated.getDepartment().getDepartmentName()).isEqualTo(updateParameters.getDepartmentName());
      verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getId()).isEqualTo(updated.getId())));
    }

    @Test
    @DisplayName("Updating an employee birthday succeeds without affecting other values")
    void givenValidBirthday_whenUpdate_thenUpdateOnlyBirthDay()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);

      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(UsableDateFormat.DEFAULT.getDateFormat());
      LocalDate localDate = LocalDate.parse("1979-12-03", dateFormatter);
      ZonedDateTime newBirthDay = localDate.atStartOfDay(ZoneOffset.UTC);
      EmployeeParameter updateParameters = employeeParameterTestFactory
              .builder()
              .emailAddress(null)
              .departmentName(null)
              .firstName(null)
              .lastName(null)
              .birthday(newBirthDay)
              .create();

      // Act
      employeeService.update(employee.getId(), updateParameters);

      // Assert
      Employee updated = employeeService.findById(employee.getId());
      assertThat(updated.getEmailAddress()).isEqualTo(employee.getEmailAddress());
      assertThat(updated.getFullName().getFirstName()).isEqualTo(employee.getFullName().getFirstName());
      assertThat(updated.getFullName().getLastName()).isEqualTo(employee.getFullName().getLastName());
      assertThat(updated.getDepartment().getDepartmentName()).isEqualTo(employee.getDepartment().getDepartmentName());
      assertThat(updated.getBirthday()).isEqualTo(newBirthDay);
      verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getId()).isEqualTo(updated.getId())));
    }

    @Test
    @DisplayName("Updating an employee full name succeeds without affecting other values")
    void givenValidFullName_whenUpdate_thenUpdateOnlyFullName()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);

      String expectedFirstName = RandomStringUtils.randomAlphabetic(23);
      String expectedLastName = RandomStringUtils.randomAlphabetic(23);

      EmployeeParameter updateParameters = employeeParameterTestFactory
              .builder()
              .emailAddress(null)
              .departmentName(null)
              .firstName(expectedFirstName)
              .lastName(expectedLastName)
              .birthday(null)
              .create();

      // Act
      employeeService.update(employee.getId(), updateParameters);

      // Assert
      Employee updated = employeeService.findById(employee.getId());
      assertThat(updated.getEmailAddress()).isEqualTo(employee.getEmailAddress());
      assertThat(updated.getFullName().getFirstName()).isEqualTo(expectedFirstName);
      assertThat(updated.getFullName().getLastName()).isEqualTo(expectedLastName);
      assertThat(updated.getDepartment().getDepartmentName()).isEqualTo(employee.getDepartment().getDepartmentName());
      assertThat(updated.getBirthday()).isEqualTo(employee.getBirthday());
      verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getId()).isEqualTo(updated.getId())));
    }

    @Test
    @DisplayName("Updating an employee email succeeds without affecting other values")
    void givenValidEmail_whenUpdate_thenUpdateOnlyEmail()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);

      String expectedEmail = employeeParameterTestFactory.builder().generateRandomEmail();

      EmployeeParameter updateParameters = employeeParameterTestFactory
              .builder()
              .emailAddress(expectedEmail)
              .departmentName(null)
              .firstName(null)
              .lastName(null)
              .birthday(null)
              .create();

      // Act
      employeeService.update(employee.getId(), updateParameters);

      // Assert
      Employee updated = employeeService.findById(employee.getId());
      assertThat(updated.getEmailAddress()).isEqualTo(expectedEmail);
      assertThat(updated.getFullName().getFirstName()).isEqualTo(employee.getFullName().getFirstName());
      assertThat(updated.getFullName().getLastName()).isEqualTo(employee.getFullName().getLastName());
      assertThat(updated.getDepartment().getDepartmentName()).isEqualTo(employee.getDepartment().getDepartmentName());
      assertThat(updated.getBirthday()).isEqualTo(employee.getBirthday());
      verify(employeeEventPublisher).employeeUpdated(assertArg(publishedEmployee -> assertThat(publishedEmployee.getId()).isEqualTo(updated.getId())));
    }

    @Test
    @DisplayName("Updating an employee with wrong uuid fails")
    void givenUnknownUuid_whenUpdate_thenThrowResourceNotFoundException()
    {
      // Arrange
      EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeService.update(UUID.randomUUID(), employeeParameter));
    }

    @Test
    @DisplayName("Updating an employee with unknown department fails")
    void givenUnknownDepartment_whenUpdate_thenThrowBadRequestException()
    {
      // Arrange
      DepartmentParameter departmentParameter = departmentParameterTestFactory.createDefault();
      departmentService.create(departmentParameter);
      EmployeeParameter employeeParameter = employeeParameterTestFactory.builder().departmentName(departmentParameter.getDepartmentName()).create();
      Employee employee = employeeService.create(employeeParameter);
      EmployeeParameter update = employeeParameterTestFactory.createDefault();

      // Act / Assert
      assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeService.update(employee.getId(), update));
    }
  }
}
