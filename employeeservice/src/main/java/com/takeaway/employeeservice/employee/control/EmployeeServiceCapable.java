package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:52
 * <p/>
 */
public interface EmployeeServiceCapable
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Employee create(@NonNull EmployeeParameter creationParameter) throws EmployeeServiceException;

    void update(@NonNull UUID uuid, @NonNull EmployeeParameter updateParameter) throws EmployeeServiceException;

    Optional<Employee> findByid(@NonNull UUID id);

    void deleteByUuid(@NonNull UUID uuid) throws EmployeeServiceException;

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
