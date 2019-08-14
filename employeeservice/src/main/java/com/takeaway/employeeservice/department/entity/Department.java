package com.takeaway.employeeservice.department.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.springintegationsupport.entity.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:30
 * <p/>
 */
@Audited
@AuditOverride(forClass = AbstractEntity.class)
@Getter
@Setter
@ToString(exclude = "employees")
@EqualsAndHashCode(exclude = {"employees"})
@Entity
@Table(name = "DEPARTMENTS", schema = "data")
@SequenceGenerator(name = "department_sequence",
        allocationSize = 1, sequenceName = "department_sequence", schema = "data")
public class Department extends AbstractEntity<Long>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Getter(onMethod = @__(@Override))
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_sequence")
    private Long id;

    @Column(name = "DEPARTMENT_NAME",
            length = 50,
            nullable = false,
            unique = true)
    private String departmentName;

    @JsonBackReference
    @OneToMany(mappedBy = "department",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Employee> employees = new HashSet<>();

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
