package com.takeaway.eventservice.messaging;

import com.takeaway.eventservice.UnitTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko
 * Date: 21.03.2019
 * Time: 12:17
 * <p/>
 */
@DisplayName("Unit tests for the employee message receiver ")
class EmployeeMessageReceiverTest extends UnitTestSuite
{
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmployeeMessageReceiver employeeMessageReceiver;

    @DisplayName("Receiving an employee message should trigger an employee event")
    @Test
    void givenEmployeeMessage_whenReceive_thenPublish()
    {
        // Arrange
        EmployeeMessage employeeMessage = employeeMessageTestFactory.createDefault();
        doNothing().when(eventPublisher)
                   .publishEvent(any());

        // Act
        employeeMessageReceiver.receiveEmployeeMessage(employeeMessage);

        // Assert
        verify(eventPublisher).publishEvent(assertArg(event -> {
            assertThat(event).isInstanceOf(EmployeeEvent.class);
            EmployeeEvent employeeEvent = (EmployeeEvent) event;
            assertThat(employeeEvent.getEmployee()).isEqualTo(employeeMessage.getEmployee());
            assertThat(employeeEvent.getEventType()).isEqualTo(employeeMessage.getEventType());
        }));
    }
}