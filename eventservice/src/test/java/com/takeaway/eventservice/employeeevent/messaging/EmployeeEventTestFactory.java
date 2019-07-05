package com.takeaway.eventservice.employeeevent.messaging;

import com.takeaway.eventservice.employeeevent.messaging.dto.EmployeeTestFactory;
import com.takeaway.eventservice.employeeevent.messaging.entity.Employee;
import com.takeaway.eventservice.employeeevent.messaging.entity.EmployeeEvent;
import com.takeaway.eventservice.employeeevent.messaging.entity.EventType;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: StMinko
 * Date: 21.03.2019
 * Time: 11:45
 * <p/>
 */
@Component
public class EmployeeEventTestFactory
{

    public EmployeeEvent createDefault()
    {
        return builder().create();
    }

    public EmployeeEventTestFactory.Builder builder()
    {
        return new Builder();
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

        public Builder employee(Employee employee)
        {
            this.employee = employee;
            return this;
        }

        public Builder eventType(EventType eventType)
        {
            this.eventType = eventType;
            return this;
        }

        public EmployeeEvent create()
        {
            return new EmployeeEvent(employee, eventType);
        }
    }
}