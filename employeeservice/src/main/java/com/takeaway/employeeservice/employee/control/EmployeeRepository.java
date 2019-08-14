package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:47
 * <p/>
 */
@Repository
interface EmployeeRepository extends JpaRepository<Employee, UUID>, RevisionRepository<Employee, UUID, Long>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================
    
    List<Employee> findByEmailAddress(String emailAddress);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
