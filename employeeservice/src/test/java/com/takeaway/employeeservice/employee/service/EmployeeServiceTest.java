package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.UnitTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

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
            verify(employeeEventPublisher).employeeDeleted(assertArg(publishedEmployee -> {
                assertThat(publishedEmployee.getBirthday()).isEqualTo(employee.getBirthday());
                assertThat(publishedEmployee.getUuid()).isEqualTo(employee.getUuid());
                assertThat(publishedEmployee.getDepartment()
                                            .getId()).isEqualTo(employee.getDepartment()
                                                                        .getId());
                assertThat(publishedEmployee.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                assertThat(publishedEmployee.getFullName()).isEqualTo(employee.getFullName());
            }));
        }
    }

    @Nested
    @DisplayName("when update")
    class WhenUpdate
    {
        @Test
        @DisplayName("Updating an employee with a wrong uuid fails")
        void givenUnknownUuid_whenUpdate_thenThrowException()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            doReturn(Optional.empty()).when(employeeRepository)
                                      .findByUuid(any());

            EmployeeParameter employeeParameter = employeeParameterTestFactory.createDefault();

            // Act / Assert
            assertThatExceptionOfType(EmployeeServiceException.class).isThrownBy(() -> employeeService.update(uuid, employeeParameter));
        }
    }

    @Nested
    @DisplayName("when new")
    class WhenNew
    {

    }
}