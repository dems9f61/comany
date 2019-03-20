package com.takeaway.eventservice.messaging;

import com.takeaway.eventservice.employee_event.service.PersistentEmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import com.takeaway.eventservice.messaging.dto.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 17:47
 * <p/>
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class EmployeeEvent extends ApplicationEvent
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final Employee employee;

    private final EventType eventType;

    // ============================  Constructors  ===========================

    public EmployeeEvent(@NonNull Employee employee, @NonNull EventType eventType)
    {
        super(employee);
        this.employee = employee;
        this.eventType = eventType;
    }

    public PersistentEmployeeEvent toPersistent()
    {
        PersistentEmployeeEvent persistentEmployeeEvent = new PersistentEmployeeEvent();
        persistentEmployeeEvent.setEventType(eventType);
        persistentEmployeeEvent.setBirthday(employee.getBirthday());
        persistentEmployeeEvent.setEmailAddress(employee.getEmailAddress());
        persistentEmployeeEvent.setFirstName(employee.getFullName()
                                                     .getFirstName());
        persistentEmployeeEvent.setLastName(employee.getFullName()
                                                    .getLastName());
        persistentEmployeeEvent.setUuid(employee.getUuid());
        persistentEmployeeEvent.setDepartmentName(employee.getDepartment()
                                                          .getDepartmentName());
        return persistentEmployeeEvent;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
