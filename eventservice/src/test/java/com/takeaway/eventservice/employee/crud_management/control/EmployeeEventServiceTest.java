package com.takeaway.eventservice.employee.crud_management.control;

import com.takeaway.eventservice.UnitTestSuite;
import com.takeaway.eventservice.employee.crud_management.entity.PersistentEmployeeEvent;
import com.takeaway.eventservice.employee.messaging.entity.Employee;
import com.takeaway.eventservice.employee.messaging.entity.EmployeeEvent;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * User: StMinko Date: 21.03.2019 Time: 09:54
 *
 * <p>
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
            doReturn(null).when(employeeEventRepository).save(any());

            // Act
            employeeEventService.handleEmployeeEvent(employeeEvent);

            // Assert
            verify(employeeEventRepository)
                    .save(assertArg(persistentEmployeeEvent -> {
                                        Employee employee = employeeEvent.getEmployee();
                                        assertThat(persistentEmployeeEvent.getDepartmentName()).isEqualTo(employee.getDepartment().getDepartmentName());
                                        assertThat(persistentEmployeeEvent.getFirstName()).isEqualTo(employee.getFullName().getFirstName());
                                        assertThat(persistentEmployeeEvent.getLastName()).isEqualTo(employee.getFullName().getLastName());
                                        assertThat(persistentEmployeeEvent.getEmployeeId()).isEqualTo(employee.getId());
                                        assertThat(persistentEmployeeEvent.getEmailAddress()).isEqualTo(employee.getEmailAddress());
                                        assertThat(persistentEmployeeEvent.getEventType()).isEqualTo(employeeEvent.getEventType());
                                        assertThat(persistentEmployeeEvent.getBirthday()).isEqualTo(Date.from(employee.getBirthday().toInstant()));
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
            UUID employeeId = UUID.randomUUID();
            Pageable mockPageable = mock(Pageable.class);
            int expectedPageNumber = RandomUtils.nextInt(0, 23);
            doReturn(expectedPageNumber).when(mockPageable).getPageNumber();

            Page<PersistentEmployeeEvent> mockPageableResult = (Page<PersistentEmployeeEvent>) mock(Page.class);
            doReturn(mockPageableResult).when(employeeEventRepository).findByEmployeeId(eq(employeeId), any(Pageable.class));

            // Act
            employeeEventService.findByEmployeeIdOrderByCreatedAtAsc(employeeId, mockPageable);

            // Assert
            verify(employeeEventRepository)
                    .findByEmployeeId(eq(employeeId),
                            assertArg(pageable -> {
                                        assertThat(pageable.getPageNumber()).isEqualTo(expectedPageNumber);
                                        assertThat(pageable.getPageSize()).isEqualTo(EmployeeEventService.MAX_PAGE_SIZE);
                                        assertThat(pageable.getSort()).isEqualTo(EmployeeEventService.CREATED_AT_WITH_ASC_SORT);
                                    }));
        }
    }
}
