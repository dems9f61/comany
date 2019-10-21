package com.takeaway.eventservice.employee.crud_management.entity;

import com.takeaway.eventservice.employee.messaging.entity.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:33
 * <p/>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "employeeenvents")
public class PersistentEmployeeEvent
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    private String id;

    private EventType eventType;

    private UUID uuid;

    private String emailAddress;

    private String firstName;

    private String lastName;

    private Date birthday;

    private String departmentName;

    @CreatedDate
    private Instant createdAt;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
