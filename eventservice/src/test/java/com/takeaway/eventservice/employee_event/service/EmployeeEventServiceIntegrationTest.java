package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.IntegrationTestSuite;
import com.takeaway.eventservice.messaging.EmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import com.takeaway.eventservice.messaging.dto.EventType;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

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
    EmployeeEventRepository employeeEventRepository;

    @Autowired
    private EmployeeEventService employeeEventService;

    @DisplayName("All published employee events appear in ascending order")
    @Test
    void givenPublishedEmployeeEventsForAnyEmployee_whenFindAll_thenReturnDescendingOrderedList()
    {
        // Arrange
        String uuid = UUID.randomUUID()
                          .toString();
        publishRandomEventsFor(uuid);

        // Act
        List<PersistentEmployeeEvent> allDescOrderedById = employeeEventService.findAllByOrderByIdAsc(uuid);

        // Assert
        long previousId = Long.MIN_VALUE;
        for (PersistentEmployeeEvent event : allDescOrderedById)
        {
            long currentId = event.getId();
            assertThat(previousId).isLessThan(currentId);
            previousId = currentId;
        }
    }

    @DisplayName("handled employee events must be persisted")
    @Test
    void givenEmployeeEvent_whenHandleEvent_thenPersistEvent()
    {
        // Arrange
        employeeEventRepository.deleteAll();
        List<Employee> employeeList = employeeTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
        EventType[] values = EventType.values();
        List<EmployeeEvent> employeeEvents = employeeList.stream()
                                                         .map(employee -> {
                                                             Random random = new Random();
                                                             EventType value = values[random.nextInt(values.length)];
                                                             return new EmployeeEvent(employee, value);
                                                         })
                                                         .collect(Collectors.toList());

        // Act
        employeeEvents.forEach(employeeEvent -> employeeEventService.handleEmployeeEvent(employeeEvent));

        // Assert
        assertThat(employeeEventRepository.findAll()).hasSameSizeAs(employeeEvents);
    }
}