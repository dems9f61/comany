package com.takeaway.eventservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.springframework.amqp.core.ExchangeTypes.TOPIC;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 14:26
 * <p/>
 */
@Component
@Slf4j
@RequiredArgsConstructor
class EmployeeEventReceiver
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "employee-deleted", durable = "true"), exchange = @Exchange(value = "${exchange.exchange-name}", type = TOPIC), key = "#.employee.deleted"))
    public void receiveEmployeeDeletedMessage(EmployeeMessage message)
    {
        LOGGER.info("###### Received Message on deleted employee ##### {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "employee-updated", durable = "true"), exchange = @Exchange(value = "${exchange.exchange-name}", type = TOPIC), key = "#.employee.updated"))
    public void receiveEmployeeUpdatedMessage(EmployeeMessage message)
    {
        LOGGER.info("###### Received Message on updated employee ##### {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "employee-updated", durable = "true"), exchange = @Exchange(value = "${exchange.exchange-name}", type = TOPIC), key = "#.employee.updated"))
    public void receiveEmployeeCreatedMessage(EmployeeMessage message)
    {
        LOGGER.info("###### Received Message on created employee ##### {}", message);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
