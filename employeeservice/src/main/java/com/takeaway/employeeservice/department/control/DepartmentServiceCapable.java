package com.takeaway.employeeservice.department.control;

import com.takeaway.employeeservice.department.entity.Department;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 16:16
 * <p/>
 */
public interface DepartmentServiceCapable
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    Department create(@NonNull DepartmentParameter departmentParameter) throws DepartmentServiceException;

    Optional<Department> findByDepartmentName(@NonNull String departmentName) throws DepartmentServiceException;

    List<Department> findAll();

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
