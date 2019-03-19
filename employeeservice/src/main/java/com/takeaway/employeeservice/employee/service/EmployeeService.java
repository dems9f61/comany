package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.service.DepartmentServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    private final EmployeeEventPublisher messagePublisher;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public Employee create(@NonNull EmployeeParameter creationParameter) throws EmployeeServiceException
    {
        LOGGER.info("Creating an employee with {} ", creationParameter);
        String departmentName = creationParameter.getDepartmentName();
        Optional<Department> departmentOptional = findDepartmentByName(departmentName);
        String emailAddress = creationParameter.getEmailAddress();
        List<Employee> employeesWithSameEmail = employeeRepository.findByEmailAddress(emailAddress);

        if (!employeesWithSameEmail.isEmpty())
        {
            throw new EmployeeServiceException(String.format("Email '%s' is already used", emailAddress));
        }

        return departmentOptional.map(department -> {
            Employee newEmployee = new Employee();
            newEmployee.setEmailAddress(emailAddress);

            Employee.FullName fullName = new Employee.FullName();
            fullName.setFirstName(creationParameter.getFirstName());
            fullName.setLastName(creationParameter.getLastName());
            newEmployee.setFullName(fullName);

            newEmployee.setBirthday(Date.from(creationParameter.getBirthday()
                                                               .atStartOfDay(ZoneId.systemDefault())
                                                               .toInstant()));
            newEmployee.setDepartment(department);
            Employee savedEmployee = employeeRepository.save(newEmployee);

            messagePublisher.employeeCreated(savedEmployee);
            return savedEmployee;
        })
                                 .orElseThrow(() -> new EmployeeServiceException(String.format("Department name '%s' could not be found!",
                                                                                               departmentName)));
    }

    //    public Employee update(@NonNull String uuid, @NonNull EmployeeParameter updateParameter) throws EmployeeServiceException
    //    {
    //        LOGGER.info("Updating an employeeToUpdate {} with {} ", uuid, updateParameter);
    //        Optional<Employee> optionalEmployee = findByUuid(uuid);
    //        if(!optionalEmployee.isPresent()){
    //            throw new EmployeeServiceException(String.format("Employee with uuid '%s' could not be found!", uuid))
    //        }
    //
    //        Employee employee = optionalEmployee.get();
    //        String newEmailAddress = updateParameter.getEmailAddress();
    //        if(!StringUtils.equals(employee.getEmailAddress(), newEmailAddress)){
    //            employee.setEmailAddress(newEmailAddress);
    //        }
    //
    //        String firstName = updateParameter.getFirstName();
    //        String lastName = updateParameter.getLastName();
    //        Employee.FullName oldFullName = employee.getFullName();
    //        if (oldFullName.equals(new ))
    //        {
    //            employee.setEmailAddress(newEmailAddress);
    //        }
    //
    //    }

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
            throw new EmployeeServiceException(String.format("Employee with uuid '%s' could not be foubnd!", uuid));
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================

    private Optional<Department> findDepartmentByName(String departmentName) throws EmployeeServiceException
    {
        if (StringUtils.isBlank(departmentName))
        {
            throw new EmployeeServiceException("Department name is blank!");
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

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
