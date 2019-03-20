package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.config.MessagingConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 13:45
 * <p/>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeEventPublisher
{
    // =========================== Class Variables ===========================
    enum Action
    {
        CREATION("takeaway.employee.created"),

        UPDATE("takeaway.employee.updated"),

        DELETION("takeaway.employee.deleted");

        private final String message;

        Action(String message)
        {
            this.message = message;
        }
    }

    // =============================  Variables  =============================

    private final RabbitTemplate template;

    private final MessagingConfig messagingConfig;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void employeeCreated(Employee employee)
    {
        LOGGER.info("Sending creation message on {}", employee);
        EmployeeCreatedMessage createdMessage = new EmployeeCreatedMessage();
        createdMessage.setEmployee(employee);
        template.convertAndSend(messagingConfig.getExchangeName(), Action.CREATION.message, createdMessage);
    }

    public void employeeDeleted(Employee employee)
    {
        LOGGER.info("Sending deletion message on {}", employee);
        EmployeeDeletedMessage deletedMessage = new EmployeeDeletedMessage();
        deletedMessage.setEmployee(employee);
        template.convertAndSend(messagingConfig.getExchangeName(), Action.DELETION.message, deletedMessage);
    }

    public void employeeUpdated(Employee employee)
    {
        LOGGER.info("Sending update message on {}", employee);
        EmployeeUpdatedMessage updatedMessage = new EmployeeUpdatedMessage();
        updatedMessage.setEmployee(employee);
        template.convertAndSend(messagingConfig.getExchangeName(), Action.UPDATE.message, updatedMessage);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Data
    private static class EmployeeCreatedMessage
    {
        private Employee employee;
    }

    @Data
    private static class EmployeeUpdatedMessage
    {
        private Employee employee;
    }

    @Data
    private static class EmployeeDeletedMessage
    {
        private Employee employee;
    }

    // ============================  End of class  ===========================
}
