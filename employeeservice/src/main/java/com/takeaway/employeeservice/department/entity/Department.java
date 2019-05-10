package com.takeaway.employeeservice.department.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.takeaway.employeeservice.employee.entity.Employee;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:30
 * <p/>
 */

@Getter
@Setter
@ToString(exclude = "employees")
@EqualsAndHashCode(exclude = {"employees", "version"})
@Entity
@Table(name = "DEPARTMENTS")
public class Department
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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

    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false)
    @Version
    private long version;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
