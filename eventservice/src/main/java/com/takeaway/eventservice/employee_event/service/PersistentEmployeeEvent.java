package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.messaging.dto.EventType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Document(collection="employeeenvents")
public class PersistentEmployeeEvent
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    private String id;

    private EventType eventType;

    private String uuid;

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
