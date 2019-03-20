package com.takeaway.eventservice.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 15:59
 * <p/>
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeMessage
{
    // =========================== Class Variables ===========================

    public enum EventType
    {
        EMPLOYEE_CREATED,

        EMPLOYEE_UPDATED,

        EMPLOYEE_DELETED;
    }

    // =============================  Variables  =============================

    private EventType eventType;

    private Employee employee;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
