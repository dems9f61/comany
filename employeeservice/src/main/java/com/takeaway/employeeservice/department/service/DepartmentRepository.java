package com.takeaway.employeeservice.department.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    List<Department> findByDepartmentName(String departmentName);

    Optional<Department> findById(long departmentId);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
