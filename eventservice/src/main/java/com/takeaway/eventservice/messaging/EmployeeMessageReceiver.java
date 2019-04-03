package com.takeaway.eventservice.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 14:26
 * <p/>
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

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${rabbitmq.queue-name}", durable = "true"),
                                             exchange = @Exchange(value = "${rabbitmq.exchange-name}"),
                                             key = "${rabbitmq.routing-key}"))
    public void receiveEmployeeMessage(@NonNull EmployeeMessage message)
    {
        LOGGER.info("###### Received Message on employee ##### {}", message);
        eventPublisher.publishEvent(new EmployeeEvent(message.getEmployee(), message.getEventType()));
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
