package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.service.DepartmentServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.takeaway.employeeservice.employee.service.EmployeeServiceException.Reason.INVALID_REQUEST;
import static com.takeaway.employeeservice.employee.service.EmployeeServiceException.Reason.NOT_FOUND;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 09:52
 * <p/>
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
class EmployeeService implements EmployeeServiceCapable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final EmployeeRepository employeeRepository;

    private final DepartmentServiceCapable departmentService;

    private final EmployeeEventPublisherCapable messagePublisher;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public Employee create(@NonNull EmployeeParameter creationParameter) throws EmployeeServiceException
    {
        LOGGER.info("Creating an employee with {} ", creationParameter);
        String emailAddress = StringUtils.trim(creationParameter.getEmailAddress());
        validateUniquenessOfEmail(emailAddress);

        String departmentName = StringUtils.trim(creationParameter.getDepartmentName());
        Optional<Department> departmentOptional = findDepartmentByName(departmentName);
        return departmentOptional.map(department -> {
            Employee newEmployee = new Employee();
            newEmployee.setEmailAddress(emailAddress);

            Employee.FullName fullName = new Employee.FullName();
            fullName.setFirstName(StringUtils.trim(creationParameter.getFirstName()));
            fullName.setLastName(StringUtils.trim(creationParameter.getLastName()));
            newEmployee.setFullName(fullName);
            newEmployee.setBirthday(creationParameter.getBirthday());
            newEmployee.setDepartment(department);
            Employee savedEmployee = employeeRepository.save(newEmployee);

            messagePublisher.employeeCreated(savedEmployee);
            return savedEmployee;
        })
                                 .orElseThrow(() -> new EmployeeServiceException(NOT_FOUND, String.format("Department name '%s' could not be found!",
                                                                                               departmentName)));
    }

    public void update(@NonNull String uuid, @NonNull EmployeeParameter updateParameter) throws EmployeeServiceException
    {
        LOGGER.info("Updating an employeeToUpdate {} with {} ", uuid, updateParameter);
        Optional<Employee> optionalEmployee = findByUuid(uuid);
        if (!optionalEmployee.isPresent())
        {
            throw new EmployeeServiceException(NOT_FOUND, String.format("Employee with uuid '%s' could not be found!", uuid));
        }

        Employee employee = optionalEmployee.get();
        boolean hasUpdated = updateEmailAddress(updateParameter, employee);
        hasUpdated = updateFullName(updateParameter, employee) || hasUpdated;
        hasUpdated = updateBirthDay(updateParameter, employee) || hasUpdated;
        hasUpdated = updateDepartment(updateParameter, employee) || hasUpdated;
        if (hasUpdated)
        {
            Employee updatedEmployee = employeeRepository.save(employee);
            messagePublisher.employeeUpdated(updatedEmployee);
        }
    }

    public Optional<Employee> findByUuid(@NonNull String uuid)
    {
        LOGGER.info("Finding an employee with {} ", uuid);
        return employeeRepository.findByUuid(uuid);
    }

    public void deleteByUuid(@NonNull String uuid) throws EmployeeServiceException
    {
        LOGGER.info("Deleting an employee with {} ", uuid);
        Optional<Employee> found = findByUuid(uuid);
        if (found.isPresent())
        {
            Employee employee = found.get();
            employeeRepository.deleteById(uuid);
            messagePublisher.employeeDeleted(employee);
        }
        else
        {
            throw new EmployeeServiceException(NOT_FOUND, String.format("Employee with uuid '%s' could not be found!", uuid));
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================

    private Optional<Department> findDepartmentByName(String departmentName) throws EmployeeServiceException
    {
        if (StringUtils.isBlank(departmentName))
        {
            throw new EmployeeServiceException(INVALID_REQUEST, "Department name is blank!");
        }

        Optional<Department> departmentOptional;
        try
        {
            departmentOptional = departmentService.findByDepartmentName(departmentName);
        }
        catch (DepartmentServiceException e)
        {
            throw new EmployeeServiceException(e);
        }
        return departmentOptional;
    }

    private boolean updateEmailAddress(EmployeeParameter updateParameter, Employee employee)
    {
        boolean hasUpdated = false;
        String newEmailAddress = updateParameter.getEmailAddress();
        if (!StringUtils.equals(employee.getEmailAddress(), newEmailAddress))
        {
            employee.setEmailAddress(newEmailAddress);
            hasUpdated = true;
        }
        return hasUpdated;
    }

    private boolean updateFullName(EmployeeParameter updateParameter, Employee employee)
    {
        boolean hasUpdated = false;
        String newFirstName = updateParameter.getFirstName();
        Employee.FullName fullName = employee.getFullName();
        if (!StringUtils.equals(fullName.getFirstName(), newFirstName))
        {
            fullName.setFirstName(newFirstName);
            hasUpdated = true;
        }
        String newLastName = updateParameter.getLastName();
        if (!StringUtils.equals(fullName.getLastName(), newLastName))
        {
            fullName.setLastName(newLastName);
            hasUpdated = true;
        }
        return hasUpdated;
    }

    private boolean updateBirthDay(EmployeeParameter updateParameter, Employee employee)
    {
        boolean hasUpdated = false;
        Date newBirthDay = updateParameter.getBirthday();
        Date oldBirthDay = employee.getBirthday();
        if (ObjectUtils.notEqual(oldBirthDay, newBirthDay))
        {
            employee.setBirthday(newBirthDay);
            hasUpdated = true;
        }
        return hasUpdated;
    }

    private boolean updateDepartment(EmployeeParameter updateParameter, Employee employee) throws EmployeeServiceException
    {
        boolean hasUpdated = false;
        String newDepartmentName = updateParameter.getDepartmentName();
        if (!StringUtils.equals(newDepartmentName,
                                employee.getDepartment()
                                        .getDepartmentName()))
        {
            try
            {
                Department department = departmentService.findByDepartmentName(newDepartmentName)
                                                         .orElseThrow(() -> new EmployeeServiceException(NOT_FOUND,
                                                                                                         String.format(
                                                                                                                 "A department with name '%s' could not be found!",
                                                                                                                 newDepartmentName)));
                employee.setDepartment(department);
            }
            catch (DepartmentServiceException e)
            {
                throw new EmployeeServiceException(e);
            }
            hasUpdated = true;
        }
        return hasUpdated;
    }

    private void validateUniquenessOfEmail(String emailAddress) throws EmployeeServiceException
    {
        List<Employee> employeesWithSameEmail = StringUtils.isBlank(emailAddress) ?
                Collections.emptyList() :
                employeeRepository.findByEmailAddress(emailAddress);
        if (!employeesWithSameEmail.isEmpty())
        {
            throw new EmployeeServiceException(INVALID_REQUEST, String.format("Email '%s' is already used", emailAddress));
        }
    }


    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
