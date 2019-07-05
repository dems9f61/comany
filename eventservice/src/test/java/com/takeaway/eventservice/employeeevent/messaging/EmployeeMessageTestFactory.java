package com.takeaway.eventservice.employeeevent.messaging;

import com.takeaway.eventservice.employeeevent.messaging.dto.EmployeeTestFactory;
import com.takeaway.eventservice.employeeevent.messaging.entity.Employee;
import com.takeaway.eventservice.employeeevent.messaging.entity.EmployeeMessage;
import com.takeaway.eventservice.employeeevent.messaging.entity.EventType;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: StMinko
 * Date: 21.03.2019
 * Time: 12:20
 * <p/>
 */
@Component
public class EmployeeMessageTestFactory
{
    public EmployeeMessage createDefault()
    {
        return builder().create();
    }

    public Builder builder()
    {
        return new EmployeeMessageTestFactory.Builder();
    }

    public static class Builder
    {
        private EmployeeTestFactory employeeTestFactory = new EmployeeTestFactory();

        private Employee employee;

        private EventType eventType;

        Builder()
        {
            employee = employeeTestFactory.createDefault();
            EventType[] values = EventType.values();
            Random random = new Random();
            eventType = values[random.nextInt(values.length)];
        }

        public EmployeeMessageTestFactory.Builder employee(Employee employee)
        {
            this.employee = employee;
            return this;
        }

        public EmployeeMessageTestFactory.Builder eventType(EventType eventType)
        {
            this.eventType = eventType;
            return this;
        }

        public EmployeeMessage create()
        {
            return new EmployeeMessage(eventType, employee);
        }
    }
}