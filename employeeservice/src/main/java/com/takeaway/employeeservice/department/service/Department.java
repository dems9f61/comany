package com.takeaway.employeeservice.department.service;

import lombok.*;

import javax.persistence.*;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:30
 * <p/>
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "DEPARTMENTS", uniqueConstraints = { @UniqueConstraint(name = "UNIQUE_NAME", columnNames = { "department_name" }) })
public class Department
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "department_name", length = 50, nullable = false)
    private String departmentName;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
