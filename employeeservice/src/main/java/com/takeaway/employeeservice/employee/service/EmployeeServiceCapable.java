package com.takeaway.employeeservice.employee.service;

import lombok.NonNull;

import java.util.Optional;

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

    void update(@NonNull String uuid, @NonNull EmployeeParameter updateParameter) throws EmployeeServiceException;

    Optional<Employee> findByUuid(@NonNull String uuid);

    void deleteByUuid(@NonNull String uuid) throws EmployeeServiceException;

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
