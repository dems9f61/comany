package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.config.MessagingConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
class EmployeeEventPublisher implements EmployeeEventPublisherCapable
{
    // =========================== Class Variables ===========================
    enum EventType
    {
        EMPLOYEE_CREATED("takeaway.employee.created"),

        EMPLOYEE_UPDATED("takeaway.employee.updated"),

        EMPLOYEE_DELETED("takeaway.employee.deleted");

        private final String message;

        EventType(String message)
        {
            this.message = message;
        }
    }

    // =============================  Variables  =============================

    private final RabbitTemplate template;

    private final MessagingConfig messagingConfig;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void employeeCreated(Employee createdEmployee)
    {
        LOGGER.info("Sending creation message on {}", createdEmployee);
        EmployeeMessage createdEmployeeMesage = new EmployeeMessage();
        createdEmployeeMesage.setEventType(EventType.EMPLOYEE_CREATED);
        createdEmployeeMesage.setEmployee(createdEmployee);
        template.convertAndSend(messagingConfig.getExchangeName(), EventType.EMPLOYEE_CREATED.message, createdEmployeeMesage);
    }

    public void employeeDeleted(Employee deletedEmployee)
    {
        LOGGER.info("Sending deletion message on {}", deletedEmployee);
        EmployeeMessage deletedEmployeeMessage = new EmployeeMessage();
        deletedEmployeeMessage.setEmployee(deletedEmployee);
        deletedEmployeeMessage.setEventType(EventType.EMPLOYEE_DELETED);
        template.convertAndSend(messagingConfig.getExchangeName(), EventType.EMPLOYEE_DELETED.message, deletedEmployeeMessage);
    }

    public void employeeUpdated(Employee updatedEmployee)
    {
        LOGGER.info("Sending update message on {}", updatedEmployee);
        EmployeeMessage updatedEmployeeMesage = new EmployeeMessage();
        updatedEmployeeMesage.setEventType(EventType.EMPLOYEE_CREATED);
        updatedEmployeeMesage.setEmployee(updatedEmployee);
        template.convertAndSend(messagingConfig.getExchangeName(), EventType.EMPLOYEE_UPDATED.message, updatedEmployeeMesage);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @ToString
    @Data
    private static class EmployeeMessage
    {
        private EventType eventType;

        private Employee  employee;
    }

    // ============================  End of class  ===========================
}
