package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.UnitTestSuite;
import com.takeaway.eventservice.messaging.EmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.UUID;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko
 * Date: 21.03.2019
 * Time: 09:54
 * <p/>
 */
@DisplayName("Unit tests for the employee event service")
class EmployeeEventServiceTest extends UnitTestSuite
{
    @Mock
    private EmployeeEventRepository employeeEventRepository;

    @InjectMocks
    private EmployeeEventService employeeEventService;

    @Nested
    @DisplayName("when Handle event")
    class WhenHandleEvent
    {
        @Test
        @DisplayName("Handle an employee events persists that event")
        void givenEmployeeVent_whenHandle_thenPersist()
        {
            // Arrange
            EmployeeEvent employeeEvent = employeeEventTestFactory.createDefault();
            doReturn(null).when(employeeEventRepository)
                          .save(any());

            // Act
            employeeEventService.handleEmployeeEvent(employeeEvent);

            // Assert
            verify(employeeEventRepository).save(assertArg(persistentEmployeeEvent -> {
                Employee employee = employeeEvent.getEmployee();
                assertThat(persistentEmployeeEvent.getDepartmentName()).isEqualTo(employee.getDepartment()
                                                                                          .getDepartmentName());
                assertThat(persistentEmployeeEvent.getFirstName()).isEqualTo(employee.getFullName()
                                                                                     .getFirstName());
                assertThat(persistentEmployeeEvent.getLastName()).isEqualTo(employee.getFullName()
                                                                                    .getLastName());
                assertThat(persistentEmployeeEvent.getUuid()).isEqualTo(employee.getUuid());
                assertThat(persistentEmployeeEvent.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                assertThat(persistentEmployeeEvent.getEventType()).isEqualTo(employeeEvent.getEventType());
                assertThat(persistentEmployeeEvent.getBirthday()).isEqualTo(employee.getBirthday());
            }));
        }
    }

    @Nested
    @DisplayName("When access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding all employee events invokes the underlying repository")
        void givenEmployeeVents_whenFindByUuid_thenInvokeRelyOnRepository()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            doReturn(Collections.emptyList()).when(employeeEventRepository)
                                             .findAllByOrderByCreatedAtAsc();

            // Act
            employeeEventService.findAllByOrderByCreatedAtAsc(uuid);

            // Assert
            verify(employeeEventRepository).findAllByOrderByCreatedAtAsc();
        }
    }
}