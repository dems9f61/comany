package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.runtime.persistence.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * User: StMinko Date: 19.03.2019 Time: 00:37
 *
 * <p>
 */
@Audited
@AuditOverride(forClass = AbstractEntity.class)
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name = "EMPLOYEES",schema = "data")
public class Employee extends AbstractEntity<UUID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    @Getter(onMethod = @__(@Override))
    @Id
    private UUID id;

    @Column(name = "EMAIL_ADDRESS",unique = true)
    private String emailAddress;

    @Embedded
    @AttributeOverrides(value = {
                @AttributeOverride(name = "firstName",column = @Column(name = "FIRST_NAME")),
                @AttributeOverride(name = "lastName",column = @Column(name = "LAST_NAME"))
            })
    private FullName fullName = new FullName();

    private ZonedDateTime birthday;

    @JsonManagedReference
    @ManyToOne(optional = false,cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DEPARTMENT_ID",nullable = false)
    private Department department;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @Override
    protected void onPrePersist()
    {
        if (isNew())
        {
            setId(UUID.randomUUID());
        }
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Setter
    @Getter
    @Embeddable
    @ToString
    @EqualsAndHashCode
    public static class FullName
    {
        private String firstName;

        private String lastName;
    }
    // ============================  End of class  ===========================
}
