package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.service.DepartmentServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 18:58
 * <p/>
 */
@DisplayName("Unit tests for the employee service")
class EmployeeServiceTest extends UnitTestSuite
{
    @Mock
    private EmployeeEventPublisher employeeEventPublisher;

    @Mock
    private DepartmentServiceCapable departmentService;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Nested
    @DisplayName("when delete")
    class WhenDelete
    {
        @Test
        @DisplayName("Deleting an employee with a wrong uuid fails")
        void givenUnknownUuid_whenDelete_thenReturnNothing()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            doReturn(Optional.empty()).when(employeeRepository)
                                      .findByUuid(any());

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.deleteByUuid(uuid));
        }

        @Test
        @DisplayName("Deleting an employee with a valid uuid succeeds")
        void givenValidUuid_whenDelete_thenSucceed() throws Exception
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            Employee employee = employeeTestFactory.createDefault();
            doReturn(Optional.of(employee)).when(employeeRepository)
                                           .findByUuid(uuid);
            doNothing().when(employeeRepository)
                       .deleteById(any());
            doNothing().when(employeeEventPublisher)
                       .employeeDeleted(any());

            // Act
            employeeService.deleteByUuid(uuid);

            // Assert
            verify(employeeRepository).deleteById(uuid);
            verify(employeeEventPublisher).employeeDeleted(assertArg(getDefaultEmployeeConsumer(employee)));
        }
    }

    @Nested
    @DisplayName("when update")
    class WhenUpdate
    {
        @Test
        @DisplayName("Updating an employee with a valid parameters succeeds")
        void giveValidParameters_whenUpdate_thenThrowException() throws Exception
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            Employee employee = employeeTestFactory.createDefault();
            doReturn(Optional.of(employee)).when(employeeRepository)
                                           .findByUuid(uuid);
            EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();
            Department department = departmentTestFactory.createDefault();

            doReturn(Optional.of(department)).when(departmentService)
                                             .findByDepartmentName(employeeParameter.getDepartmentName());
            doReturn(employee).when(employeeRepository)
                              .save(any());

            // Act
            employeeService.update(uuid, employeeParameter);

            // Assert
            verify(employeeRepository).save(assertArg(getDefaultEmployeeConsumer(employee)));
            verify(employeeEventPublisher).employeeUpdated(assertArg(getDefaultEmployeeConsumer(employee)));
        }

        @Test
        @DisplayName("Updating an employee with a wrong uuid fails")
        void givenUnknownUuid_whenUpdate_thenThrowException()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            doReturn(Optional.empty()).when(employeeRepository)
                                      .findByUuid(uuid);

            EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.update(uuid, employeeParameter));
        }

        @Test
        @DisplayName("Updating an employee with a unknown department fails")
        void givenUnknownDepartment_whenUpdate_thenThrowException() throws Exception
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            Employee employee = employeeTestFactory.createDefault();
            doReturn(Optional.of(employee)).when(employeeRepository)
                                           .findByUuid(uuid);

            EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();
            doThrow(DepartmentServiceException.class).when(departmentService)
                                                     .findByDepartmentName(employeeParameter.getDepartmentName());

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.update(uuid, employeeParameter));
        }
    }


    @Nested
    @DisplayName("when new")
    class WhenNew
    {

    }

    private Consumer<Employee> getDefaultEmployeeConsumer(Employee employee)
    {
        return candidate -> {
            assertThat(candidate.getBirthday()).isEqualTo(employee.getBirthday());
            assertThat(candidate.getUuid()).isEqualTo(employee.getUuid());
            assertThat(candidate.getDepartment()
                                .getId()).isEqualTo(employee.getDepartment()
                                                            .getId());
            assertThat(candidate.getEmailAddress()).isEqualTo(employee.getEmailAddress());
            assertThat(candidate.getFullName()).isEqualTo(employee.getFullName());
        };
    }

}