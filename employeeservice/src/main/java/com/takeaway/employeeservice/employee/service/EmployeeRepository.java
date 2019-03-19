package com.takeaway.employeeservice.employee.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:47
 * <p/>
 */
public interface EmployeeRepository extends JpaRepository<Employee, String>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Optional<Employee> findByUuid(String uuid);

    List<Employee> findByEmailAddress(String emailAddress);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
