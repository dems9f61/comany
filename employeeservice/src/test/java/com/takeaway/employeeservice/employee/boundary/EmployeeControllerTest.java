package com.takeaway.employeeservice.employee.boundary;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.employee.control.EmployeeParameter;
import com.takeaway.employeeservice.employee.control.EmployeeServiceCapable;
import com.takeaway.employeeservice.employee.control.EmployeeServiceException;
import com.takeaway.employeeservice.employee.entity.CreateEmployeeRequest;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.employee.entity.EmployeeRequest;
import com.takeaway.employeeservice.employee.entity.UpdateEmployeeRequest;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.errorhandling.entity.InternalServerErrorException;
import com.takeaway.employeeservice.errorhandling.entity.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.takeaway.employeeservice.employee.control.EmployeeServiceException.Reason.INVALID_REQUEST;
import static com.takeaway.employeeservice.employee.control.EmployeeServiceException.Reason.NOT_FOUND;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * User: StMinko Date: 20.03.2019 Time: 11:53
 *
 * <p>
 */
@DisplayName("Unit tests for the employee controller")
class EmployeeControllerTest extends UnitTestSuite
{
  @Mock
  private EmployeeServiceCapable employeeService;

  @InjectMocks
  private EmployeeController employeeController;

  @Nested
  @DisplayName("when delete")
  class WhenDelete
  {
    @Test
    @DisplayName("Deleting an employee with a wrong uuid throws ResourceNotFoundException")
    void givenUnknownUuid_whenDelete_thenThrowNotFoundException() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      doThrow(new EmployeeServiceException(NOT_FOUND, "bla bla")).when(employeeService).deleteByUuid(any());

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.deleteEmployee(uuid));
    }

    @Test
    @DisplayName("Deleting an employee throws InternalServerErrorException if something unexpected occurs")
    void givenUnknownException_whenDelete_thenThrowInternalException() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      doThrow(new EmployeeServiceException(new Exception())).when(employeeService).deleteByUuid(any());

      // Act / Assert
      assertThatExceptionOfType(InternalServerErrorException.class).isThrownBy(() -> employeeController.deleteEmployee(uuid));
    }

    @Test
    @DisplayName("Deleting an employee with a valid uuid succeeds")
    void givenValidUuid_whenDelete_thenSucceed() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();

      doNothing().when(employeeService).deleteByUuid(any());

      // Act
      employeeController.deleteEmployee(uuid);

      // Assert
      verify(employeeService).deleteByUuid(uuid);
    }
  }

  @Nested
  @DisplayName("when access")
  class WhenAccess
  {
    @Test
    @DisplayName("Finding an employee with a wrong uuid throws ResourceNotFoundException")
    void givenUnknownUuid_whenFindByUuid_thenThrowNotFoundException()
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      doReturn(Optional.empty()).when(employeeService).findByid(uuid);

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.findEmployee(uuid));
    }

    @Test
    @DisplayName("Finding an employee with a valid uuid succeeds")
    void givenValidUuid_whenFindByUuid_thenSucceed()
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      Employee employee = employeeTestFactory.createDefault();
      doReturn(Optional.of(employee)).when(employeeService).findByid(any());

      // Act
      employeeController.findEmployee(uuid);

      // Assert
      verify(employeeService).findByid(uuid);
    }
  }

  @Nested
  @DisplayName("when update")
  class WhenUpdate
  {
    @Test
    @DisplayName("Updating an employee with a valid request succeeds")
    void givenValidRequest_whenUpdate_thenSucceed() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      UpdateEmployeeRequest employeeRequest = updateEmployeeRequestTestFactory.createDefault();
      doNothing().when(employeeService).update(any(), any());

      // Act
      employeeController.updateEmployee(uuid, employeeRequest);

      // Assert
      verify(employeeService).update(eq(uuid), assertArg(getEmployeeParameterConsumer(employeeRequest)));
    }

    @Test
    @DisplayName("Updating an employee throws ResourceNotFoundException if the underlying service throws a Not Found")
    void givenUnderlyingNotFound_whenCreate_thenThrowNotFoundException() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      UpdateEmployeeRequest employeeRequest = updateEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(NOT_FOUND, "")).when(employeeService).update(uuid, employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.updateEmployee(uuid, employeeRequest));
    }

    @Test
    @DisplayName("Updating an employee throws BadRequestException if the underlying service throws a Invalid Request")
    void givenUnderlyingInvalidRequest_whenCreate_thenThrowBadRequestException() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      UpdateEmployeeRequest employeeRequest = updateEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(INVALID_REQUEST, "")).when(employeeService).update(uuid, employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeController.updateEmployee(uuid, employeeRequest));
    }

    @Test
    @DisplayName("Updating an employee throws InternalServerErrorException if the underlying service throws a unknown reason")
    void givenUnderlyingInvalidGenericException_whenUpdate_thenThrowInternalServerErrorException() throws Exception
    {
      // Arrange
      UUID uuid = UUID.randomUUID();
      UpdateEmployeeRequest employeeRequest = updateEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(new Exception())).when(employeeService).update(uuid, employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(InternalServerErrorException.class).isThrownBy(() -> employeeController.updateEmployee(uuid, employeeRequest));
    }
  }

  @Nested
  @DisplayName("when new")
  class WhenNew
  {
    @Test
    @DisplayName("Creating an employee with a valid request succeeds")
    void givenValidRequest_whenCreate_thenSucceed() throws Exception
    {
      // Arrange
      CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.createDefault();
      Employee employee = employeeTestFactory.createDefault();
      doReturn(employee).when(employeeService).create(any());

      // Act
      employeeController.createEmployee(employeeRequest);

      // Assert
      verify(employeeService).create(assertArg(getEmployeeParameterConsumer(employeeRequest)));
    }

    @Test
    @DisplayName("Creating an employee throws ResourceNotFoundException if the underlying service throws a Not Found")
    void givenUnderlyingNotFound_whenCreate_thenThrowNotFoundException() throws Exception
    {
      // Arrange
      CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(NOT_FOUND, "")).when(employeeService).create(employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.createEmployee(employeeRequest));
    }

    @Test
    @DisplayName("Creating an employee throws BadRequestException if the underlying service throws a Invalid Request")
    void givenUnderlyingInvalidRequest_whenCreate_thenThrowBadRequestException() throws Exception
    {
      // Arrange
      CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(INVALID_REQUEST, "")).when(employeeService).create(employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeController.createEmployee(employeeRequest));
    }

    @Test
    @DisplayName("Creating an employee throws InternalServerErrorException if the underlying service throws a unknown reason")
    void givenUnderlyingInvalidGenericException_whenCreate_thenThrowInternalServerErrorException() throws Exception
    {
      // Arrange
      CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.createDefault();
      doThrow(new EmployeeServiceException(new Exception())).when(employeeService).create(employeeRequest.toEmployerParameter());

      // Act / Assert
      assertThatExceptionOfType(InternalServerErrorException.class).isThrownBy(() -> employeeController.createEmployee(employeeRequest));
    }
  }

  private Consumer<EmployeeParameter> getEmployeeParameterConsumer(EmployeeRequest employeeRequest)
  {
    return employeeParameter -> {
      assertThat(employeeParameter.getBirthday()).isEqualTo(employeeRequest.getBirthday());
      assertThat(employeeParameter.getEmailAddress()).isEqualTo(employeeRequest.getEmailAddress());
      assertThat(employeeParameter.getDepartmentName()).isEqualTo(employeeRequest.getDepartmentName());
      assertThat(employeeParameter.getFirstName()).isEqualTo(employeeRequest.getFirstName());
      assertThat(employeeParameter.getLastName()).isEqualTo(employeeRequest.getLastName());
    };
  }
}
