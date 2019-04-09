package com.takeaway.employeeservice.department.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:48
 * <p/>
 */
@Repository
interface DepartmentRepository extends JpaRepository<Department, Integer>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    List<Department> findByDepartmentName(String departmentName);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
