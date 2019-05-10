package com.takeaway.eventservice.employeeevent.boundary;

import com.takeaway.eventservice.UnitTestSuite;
import com.takeaway.eventservice.employeeevent.control.EmployeeEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko
 * Date: 21.03.2019
 * Time: 09:41
 * <p/>
 */
@DisplayName("Unit tests for the employee event controller")
class EmployeeEventControllerTest extends UnitTestSuite
{
    @Mock
    private EmployeeEventService employeeEventService;

    @InjectMocks
    private EmployeeEventController employeeEventController;

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding all employee events invokes the underlying service")
        void givenEmployeeVents_whenFindByUuid_thenInvokeService()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            doReturn(Collections.emptyList()).when(employeeEventService)
                                             .findAllByOrderByCreatedAtAsc(any());

            // Act
            employeeEventController.getAllEmployeeEvents(uuid);

            // Assert
            verify(employeeEventService).findAllByOrderByCreatedAtAsc(uuid);
        }
    }
}