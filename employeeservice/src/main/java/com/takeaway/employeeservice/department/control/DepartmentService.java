package com.takeaway.employeeservice.department.control;

import com.takeaway.employeeservice.department.entity.Department;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 11:51
 * <p/>
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
class DepartmentService implements DepartmentServiceCapable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final DepartmentRepository departmentRepository;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public Optional<Department> findByDepartmentName(@NonNull String departmentName) throws DepartmentServiceException
    {
        LOGGER.info("Finding by department name [{}]", departmentName);
        List<Department> departments = departmentRepository.findByDepartmentName(departmentName);
        if (departments.size() > 1)
        {
            String errorMessage = String.format("There are multiple departments by the name '%s' already exists!", departmentName);
            throw new DepartmentServiceException(errorMessage);
        }
        return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
    }

    public List<Department> findAll()
    {
        LOGGER.info("Finding all departments");
        return departmentRepository.findAll();
    }

    @Transactional
    public Department create(@NonNull DepartmentParameter departmentParameter) throws DepartmentServiceException
    {
        LOGGER.info("Creating a department with [{}] ", departmentParameter);
        String departmentName = departmentParameter.getDepartmentName();

        if (StringUtils.isBlank(departmentName))
        {
            throw new DepartmentServiceException("Department name is blank!");
        }

        List<Department> foundDepartment = departmentRepository.findByDepartmentName(departmentName);
        if (!foundDepartment.isEmpty())
        {
            String errorMessage = String.format("Department name '%s' already exists!", departmentName);
            throw new DepartmentServiceException(errorMessage);
        }
        Department department = new Department();
        department.setDepartmentName(departmentName);
        return departmentRepository.save(department);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
