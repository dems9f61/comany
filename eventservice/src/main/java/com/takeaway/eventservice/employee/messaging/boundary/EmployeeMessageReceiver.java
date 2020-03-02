package com.takeaway.eventservice.employee.messaging.boundary;

import com.takeaway.eventservice.employee.messaging.entity.EmployeeEvent;
import com.takeaway.eventservice.employee.messaging.entity.EmployeeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 20.03.2019 Time: 14:26
 *
 * <p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EmployeeMessageReceiver
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final ApplicationEventPublisher eventPublisher;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @RabbitListener(queues = "${amqp.queue-name}")
    public void receiveEmployeeMessage(@NonNull EmployeeMessage employeeMessage)
    {
        LOGGER.info("###### Received Message on employee ##### [{}]", employeeMessage);
        // Catch any Exception to have the Message marked as processed on the Message Broker
        // Avoids infinite Loops on Message Processing
        try
        {
            LOGGER.info("Publishing pdf engine delivery [{}]", employeeMessage);
            eventPublisher.publishEvent(new EmployeeEvent(employeeMessage.getEmployee(), employeeMessage.getEventType()));
        }
        catch (Exception caught)
        {
            LOGGER.error("An error occurred during EmployeeMessageReceiver.receiveEmployeeMessage ( [{}] ). Error was: {}",
                         employeeMessage,
                         caught.getMessage(),
                         caught);
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
