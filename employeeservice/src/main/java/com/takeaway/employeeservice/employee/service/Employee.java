package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.department.service.Department;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name")) })
    private FullName fullName = new FullName();

    @Column(length = 10)
    private Date birthday;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

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
    static class FullName
    {
        private String firstName;

        private String lastName;
    }
    // ============================  End of class  ===========================
}
