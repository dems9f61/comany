package com.takeaway.employeeservice.employee.boundary;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.employee.control.EmployeeParameter;
import com.takeaway.employeeservice.employee.control.EmployeeServiceCapable;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.employee.entity.EmployeeRequest;
import com.takeaway.employeeservice.runtime.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.runtime.errorhandling.entity.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

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
        @DisplayName("Deleting an employee with a wrong id throws ResourceNotFoundException")
        void givenUnknownId_whenDelete_thenThrowNotFoundException()
        {
            // Arrange
            UUID id = UUID.randomUUID();
            doThrow(ResourceNotFoundException.class).when(employeeService).deleteById(any());

            // Act / Assert
            assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.deleteEmployee(id));
        }

        @Test
        @DisplayName("Deleting an employee with a valid id succeeds")
        void givenValidId_whenDelete_thenSucceed()
        {
            // Arrange
            UUID id = UUID.randomUUID();
            doNothing().when(employeeService).deleteById(any());

            // Act
            employeeController.deleteEmployee(id);

            // Assert
            verify(employeeService).deleteById(id);
        }
    }

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding an employee with a wrong id throws ResourceNotFoundException")
        void givenUnknownId_whenFindById_thenThrowNotFoundException()
        {
            // Arrange
            UUID id = UUID.randomUUID();
            doThrow(ResourceNotFoundException.class).when(employeeService).findById(id);

            // Act / Assert
            assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.findEmployee(id));
        }

        @Test
        @DisplayName("Finding an all employees succeeds")
        void whenFindAll_thenSucceed()
        {
            // Arrange
            doReturn(Collections.emptyList()).when(employeeService)
                                             .findAll();

            // Act
            employeeController.findAllEmployees();

            // Assert
            verify(employeeService).findAll();
        }


        @Test
        @DisplayName("Finding an employee with a valid id succeeds")
        void givenValidId_whenFindById_thenSucceed()
        {
            // Arrange
            UUID id = UUID.randomUUID();
            Employee employee = employeeTestFactory.createDefault();
            doReturn(employee).when(employeeService).findById(any());

            // Act
            employeeController.findEmployee(id);

            // Assert
            verify(employeeService).findById(id);
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
            UUID id = UUID.randomUUID();
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            doNothing().when(employeeService).update(any(), any());

            // Act
            employeeController.updateEmployee(id, employeeRequest);

            // Assert
            verify(employeeService).update(eq(id), assertArg(getEmployeeParameterConsumer(employeeRequest)));
        }

        @Test
        @DisplayName("Updating an employee throws ResourceNotFoundException if the underlying service throws ResourceNotFoundException")
        void givenUnderlyingNotFound_whenCreate_thenThrowNotFoundException() throws Exception
        {
            // Arrange
            UUID id = UUID.randomUUID();
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            doThrow(ResourceNotFoundException.class).when(employeeService).update(id, employeeRequest.toEmployerParameter());

            // Act / Assert
            assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> employeeController.updateEmployee(id, employeeRequest));
        }

        @Test
        @DisplayName("Updating an employee throws BadRequestException if the underlying service throws a BadRequestException")
        void givenUnderlyingBadRequest_whenCreate_thenThrowBadRequestException()
        {
            // Arrange
            UUID uuid = UUID.randomUUID();
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            doThrow(BadRequestException.class).when(employeeService).update(uuid, employeeRequest.toEmployerParameter());

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeController.updateEmployee(uuid, employeeRequest));
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
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            Employee employee = employeeTestFactory.createDefault();
            doReturn(employee).when(employeeService).create(any());

            // Act
            employeeController.createEmployee(employeeRequest);

            // Assert
            verify(employeeService).create(assertArg(getEmployeeParameterConsumer(employeeRequest)));
        }

        @Test
        @DisplayName("Creating an employee throws BadRequestException if the underlying service throws a BadRequestException")
        void givenUnderlyingBadRequestException_whenCreate_thenThrowBadRequestException()
        {
            // Arrange
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            doThrow(BadRequestException.class).when(employeeService).create(employeeRequest.toEmployerParameter());

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> employeeController.createEmployee(employeeRequest));
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
