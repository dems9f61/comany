package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:47
 * <p/>
 */
@Repository
interface EmployeeRepository extends JpaRepository<Employee, String>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @Lock(value = LockModeType.READ)
    Optional<Employee> findByUuid(String uuid);

    @Lock(value = LockModeType.READ)
    List<Employee> findByEmailAddress(String emailAddress);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
