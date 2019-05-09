package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.IntegrationTestSuite;
import com.takeaway.eventservice.messaging.EmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 19:00
 * <p/>
 */
@DisplayName("Integration tests for employee event service")
class EmployeeEventServiceIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private EmployeeEventRepository employeeEventRepository;

    @Autowired
    private EmployeeEventService employeeEventService;

    @DisplayName("All published employee events to a specific uuid appear in ascending order")
    @Test
    void givenPublishedEmployeeEventsForAnyEmployee_whenFindAll_thenReturnDescendingOrderedList()
    {
        // Arrange
        String uuid = UUID.randomUUID()
                          .toString();
        receiveRandomMessageFor(uuid);

        // Act
        List<PersistentEmployeeEvent> allDescOrderedByCreatedAt = employeeEventService.findAllByOrderByCreatedAtAsc(uuid);

        // Assert
        Instant previous = null;
        for (PersistentEmployeeEvent event : allDescOrderedByCreatedAt)
        {
            Instant current = event.getCreatedAt();
            if(previous != null){
                assertThat(previous).isBefore(current);
            }
            previous = current;
        }
    }

    @DisplayName("Finding published employee events returns an empty collection For a not unknown uuid ")
    @Test
    void givenUnknownUuid_whenFindAll_thenReturnEmptyList()
    {
        // Arrange
        receiveRandomMessageFor(RandomUtils.nextInt(10, 20));
        String unknownUuid = UUID.randomUUID()
                                 .toString();

        // Act
        List<PersistentEmployeeEvent> events = employeeEventService.findAllByOrderByCreatedAtAsc(unknownUuid);

        // Assert
        assertThat(events).isNotNull()
                          .isEmpty();
    }

    @DisplayName("Handling an employee events makes it persistent")
    @Test
    void givenEmployeeEvent_whenHandle_thenPersistEvent()
    {
        // Arrange
        employeeEventRepository.deleteAll();
        Employee employee = employeeTestFactory.createDefault();
        EmployeeEvent employeeEvent = employeeEventTestFactory.builder()
                                                              .employee(employee)
                                                              .create();

        // Act
        employeeEventService.handleEmployeeEvent(employeeEvent);

        // Assert
        List<PersistentEmployeeEvent> allEvents = employeeEventRepository.findAll();
        assertThat(allEvents).isNotEmpty()
                             .hasSize(1);
        PersistentEmployeeEvent persistentEmployeeEvent = allEvents.get(0);
        assertThat(persistentEmployeeEvent.getId()).isNotBlank();
        assertThat(persistentEmployeeEvent.getCreatedAt()).isNotNull()
                                                          .isBefore(Instant.now());
        assertThat(persistentEmployeeEvent.getBirthday()).isEqualTo(employee.getBirthday());
        assertThat(persistentEmployeeEvent.getDepartmentName()).isEqualTo(employee.getDepartment()
                                                                                  .getDepartmentName());
        assertThat(persistentEmployeeEvent.getEmailAddress()).isEqualTo(employee.getEmailAddress());
        assertThat(persistentEmployeeEvent.getEventType()).isNotNull()
                                                          .isEqualTo(employeeEvent.getEventType());
        assertThat(persistentEmployeeEvent.getUuid()).isEqualTo(employee.getUuid());
        assertThat(persistentEmployeeEvent.getFirstName()).isEqualTo(employee.getFullName()
                                                                             .getFirstName());
        assertThat(persistentEmployeeEvent.getLastName()).isEqualTo(employee.getFullName()
                                                                            .getLastName());
    }
}