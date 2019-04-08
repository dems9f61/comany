package com.takeaway.employeeservice.employee.service;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.takeaway.employeeservice.department.service.Department;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 00:37
 * <p/>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "EMPLOYEES")
public class Employee
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    @Id
    private String uuid;

    @Column(name = "EMAIL_ADDRESS", unique = true)
    private String emailAddress;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "firstName", column = @Column(name = "FIRST_NAME")),
            @AttributeOverride(name = "lastName", column = @Column(name = "LAST_NAME")) })
    private FullName fullName = new FullName();

    @Column(length = 7, name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @JsonManagedReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    private Department department;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    @Version
    private long version;

    // ============================  Constructors  ===========================

    Employee()
    {
        this.uuid = UUID.randomUUID()
                        .toString();
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Setter
    @Getter
    @Embeddable
    @EqualsAndHashCode
    public static class FullName
    {
        private String firstName;

        private String lastName;
    }
    // ============================  End of class  ===========================
}
