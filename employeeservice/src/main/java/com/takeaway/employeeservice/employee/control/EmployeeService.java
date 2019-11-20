package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.department.control.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * User: StMinko Date: 19.03.2019 Time: 09:52
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated
@Service
class EmployeeService implements EmployeeServiceCapable
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @Getter
  private final EmployeeRepository repository;

  private final DepartmentServiceCapable departmentService;

  private final EmployeeEventPublisherCapable messagePublisher;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public Employee create(@NonNull EmployeeParameter creationParameter)
  {
    LOGGER.info("Creating an employee with [{}] ", creationParameter);
    String emailAddress = StringUtils.trim(creationParameter.getEmailAddress());
    validateUniquenessOfEmail(emailAddress);

    String departmentName = StringUtils.trim(creationParameter.getDepartmentName());
    Department department = departmentService.findByDepartmentNameOrElseThrow(departmentName, BadRequestException.class);
    Employee newEmployee = new Employee();
    newEmployee.setEmailAddress(emailAddress);

    String firstName = StringUtils.trim(creationParameter.getFirstName());
    String lastName = StringUtils.trim(creationParameter.getLastName());
    if (!StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName))
    {
      Employee.FullName fullName = new Employee.FullName();
      fullName.setFirstName(StringUtils.trim(firstName));

      fullName.setLastName(StringUtils.trim(lastName));
      newEmployee.setFullName(fullName);
    }

    newEmployee.setBirthday(creationParameter.getBirthday());
    newEmployee.setDepartment(department);
    Employee savedEmployee = repository.save(newEmployee);

    messagePublisher.employeeCreated(savedEmployee);
    return savedEmployee;
  }

  public void update(@NonNull UUID id, @NonNull EmployeeParameter updateParameter)
  {
    LOGGER.info("Updating an employeeToUpdate [{}] with [{}] ", id, updateParameter);
    Employee employee = findById(id);
    boolean hasChanged = hasEmailAddressChangedAfterUpdate(updateParameter, employee);
    hasChanged = hasFullNameChangedAfterUpdate(updateParameter, employee) || hasChanged;
    hasChanged = hasBirthDayChangedAfterUpdate(updateParameter, employee) || hasChanged;
    hasChanged = hasDepartmentChangedAfterUpdate(updateParameter, employee) || hasChanged;
    if (hasChanged)
    {
      Employee updatedEmployee = repository.save(employee);
      messagePublisher.employeeUpdated(updatedEmployee);
    }
  }

  public void deleteById(@NonNull UUID id)
  {
    LOGGER.info("Deleting an employee with [{}] ", id);
    Employee employee = findById(id);
    repository.deleteById(id);
    messagePublisher.employeeDeleted(employee);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================

  private void validateUniquenessOfEmail(String emailAddress)
  {
    List<Employee> employeesWithSameEmail = StringUtils.isBlank(emailAddress) ? Collections.emptyList() : repository.findByEmailAddress(emailAddress);
    if (!employeesWithSameEmail.isEmpty())
    {
      throw new BadRequestException(String.format("Email [%s] is already used", emailAddress));
    }
  }

  private boolean hasEmailAddressChangedAfterUpdate(EmployeeParameter updateParameter, Employee employee)
  {
    boolean hasUpdated = false;
    String newEmailAddress = StringUtils.trim(updateParameter.getEmailAddress());
    if (StringUtils.isNotBlank(newEmailAddress) && !StringUtils.equals(employee.getEmailAddress(), newEmailAddress))
    {
      employee.setEmailAddress(newEmailAddress);
      hasUpdated = true;
    }
    return hasUpdated;
  }

  private boolean hasFullNameChangedAfterUpdate(EmployeeParameter updateParameter, Employee employee)
  {
    boolean hasUpdated = false;
    String newFirstName = StringUtils.trim(updateParameter.getFirstName());
    String newLastName = StringUtils.trim(updateParameter.getLastName());
    Employee.FullName fullName = employee.getFullName();
    if (fullName != null)
    {
      if (StringUtils.isNotBlank(newFirstName) && !StringUtils.equals(fullName.getFirstName(), newFirstName))
      {
        fullName.setFirstName(newFirstName);
        hasUpdated = true;
      }
      if (StringUtils.isNotBlank(newLastName) && !StringUtils.equals(fullName.getLastName(), newLastName))
      {
        fullName.setLastName(newLastName);
        hasUpdated = true;
      }
    }
    else
    {
      if (StringUtils.isNotBlank(newFirstName) || StringUtils.isNotBlank(newLastName))
      {
        fullName = new Employee.FullName();
        fullName.setFirstName(newFirstName);
        fullName.setLastName(newLastName);
        employee.setFullName(fullName);
        hasUpdated = true;
      }
    }

    return hasUpdated;
  }

  private boolean hasBirthDayChangedAfterUpdate(EmployeeParameter updateParameter, Employee employee)
  {
    boolean hasUpdated = false;
    ZonedDateTime newBirthDay = updateParameter.getBirthday();
    ZonedDateTime oldBirthDay = employee.getBirthday();
    if (Objects.nonNull(newBirthDay) && ObjectUtils.notEqual(oldBirthDay, newBirthDay))
    {
      employee.setBirthday(newBirthDay);
      hasUpdated = true;
    }
    return hasUpdated;
  }

  private boolean hasDepartmentChangedAfterUpdate(EmployeeParameter updateParameter, Employee employee)
  {
    boolean hasUpdated = false;
    String newDepartmentName = StringUtils.trim(updateParameter.getDepartmentName());
    if (StringUtils.isNotBlank(newDepartmentName) && !StringUtils.equals(newDepartmentName, employee.getDepartment().getDepartmentName()))
    {
      Department department = departmentService.findByDepartmentNameOrElseThrow(newDepartmentName, BadRequestException.class);
      employee.setDepartment(department);
      hasUpdated = true;
    }
    return hasUpdated;
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
