package com.takeaway.employeeservice.department.control;

import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.errorhandling.entity.ResourceNotFoundException;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * User: StMinko Date: 18.03.2019 Time: 16:16
 *
 * <p>
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface DepartmentServiceCapable
{
  // =========================== Class Variables ===========================

  Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceCapable.class);

  // ==============================  Methods  ==============================

  DepartmentRepository getRepository();

  default Department create(@NonNull DepartmentParameter departmentParameter)
  {
    LOGGER.info("Creating a department with [{}] ", departmentParameter);
    String departmentName = departmentParameter.getDepartmentName();

    if (StringUtils.isBlank(departmentName))
    {
      throw new BadRequestException("Department name is blank!");
    }

    List<Department> foundDepartment = getRepository().findByDepartmentName(departmentName);
    if (!foundDepartment.isEmpty())
    {
      String errorMessage = String.format("Department name [%s] already exists!", departmentName);
      throw new BadRequestException(errorMessage);
    }
    Department department = new Department();
    department.setDepartmentName(departmentName);
    return getRepository().save(department);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  default Department findByDepartmentName(@NonNull String departmentName)
  {
    LOGGER.info("Finding department by name [{}]", departmentName);
    return findByDepartmentNameOrElseThrow(departmentName, ResourceNotFoundException.class);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  default Department findByDepartmentNameOrElseThrow(@NotNull String departmentName, @NotNull Class<? extends RuntimeException> exceptionClass)
  {
    LOGGER.info("Finding department by name [{}] or throw [{}]", departmentName, exceptionClass.getSimpleName());
    List<Department> departments = getRepository().findByDepartmentName(departmentName);
    if (departments.size() > 1)
    {
      String errorMessage = String.format("There are multiple departments by the name [%s] already exists!", departmentName);
      return throwExceptionFor(exceptionClass, errorMessage);
    }
    if (departments.isEmpty())
    {
      String errorMessage = String.format("Could not find Department by the name [%s]!", departmentName);
      return throwExceptionFor(exceptionClass, errorMessage);
    }

    return departments.get(0);
  }

  default Department throwExceptionFor(@NotNull Class<? extends RuntimeException> exceptionClass, String errorMessage)
  {
    try
    {
      throw exceptionClass.getConstructor(String.class).newInstance(errorMessage);
    }
    catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
    {
      throw new RuntimeException(errorMessage);
    }
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  default List<Department> findAll()
  {
    LOGGER.info("Finding all departments");
    return getRepository().findAll();
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
