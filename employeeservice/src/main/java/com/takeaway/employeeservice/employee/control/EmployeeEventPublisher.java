package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.employee.entity.EmployeeMessage;
import com.takeaway.employeeservice.runtime.config.MessagingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 19.03.2019 Time: 13:45
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Component
class EmployeeEventPublisher implements EmployeeEventPublisherCapable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final RabbitTemplate template;

    private final MessagingConfig messagingConfig;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void employeeCreated(Employee createdEmployee)
    {
        LOGGER.info("Sending creation message [{}]", createdEmployee);
        EmployeeMessage createdEmployeeMessage = new EmployeeMessage();
        createdEmployeeMessage.setEventType(EmployeeMessage.EventType.EMPLOYEE_CREATED);
        createdEmployeeMessage.setEmployee(createdEmployee);
        template.convertAndSend(messagingConfig.getExchangeName(), messagingConfig.getRoutingKey(), createdEmployeeMessage);
    }

    public void employeeDeleted(Employee deletedEmployee)
    {
        LOGGER.info("Sending deletion message {}", deletedEmployee);
        EmployeeMessage deletedEmployeeMessage = new EmployeeMessage();
        deletedEmployeeMessage.setEmployee(deletedEmployee);
        deletedEmployeeMessage.setEventType(EmployeeMessage.EventType.EMPLOYEE_DELETED);
        template.convertAndSend(messagingConfig.getExchangeName(), messagingConfig.getRoutingKey(), deletedEmployeeMessage);
    }

    public void employeeUpdated(Employee updatedEmployee)
    {
        LOGGER.info("Sending update message [{}]", updatedEmployee);
        EmployeeMessage updatedEmployeeMessage = new EmployeeMessage();
        updatedEmployeeMessage.setEventType(EmployeeMessage.EventType.EMPLOYEE_UPDATED);
        updatedEmployeeMessage.setEmployee(updatedEmployee);
        template.convertAndSend(messagingConfig.getExchangeName(), messagingConfig.getRoutingKey(), updatedEmployeeMessage);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    // ============================  End of class  ===========================
}
