package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.messaging.dto.EventType;
import lombok.*;

import javax.persistence.*;
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
@Entity
@Table(name = "EMPLOYEE_EVENTS")
public class PersistentEmployeeEvent
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private EventType eventType;

    @Column(name = "uuid", updatable = false)
    private String uuid;

    @Column(name = "email_address", updatable = false)
    private String emailAddress;

    @Column(name = "first_name", updatable = false)
    private String firstName;

    @Column(name = "last_name", updatable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(length = 10, updatable = false)
    private Date birthday;

    @Column(name = "department_name", updatable = false)
    private String departmentName;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
