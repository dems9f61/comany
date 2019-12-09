package com.takeaway.eventservice.employee.messaging.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * User: StMinko Date: 20.03.2019 Time: 17:47
 *
 * <p>
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

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
