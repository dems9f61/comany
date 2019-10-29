package com.takeaway.employeeservice.employee.entity;

import com.takeaway.employeeservice.AbstractTestFactory;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.department.entity.DepartmentTestFactory;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * User: StMinko Date: 19.03.2019 Time: 19:04
 *
 * <p>
 */
public class EmployeeTestFactory extends AbstractTestFactory<Employee, EmployeeTestFactory.Builder>
{
  public Builder builder()
  {
    return new Builder();
  }

  public static class Builder implements AbstractTestFactory.Builder<Employee>
  {
    private String emailAddress;

    private Employee.FullName fullName;

    private Date birthday;

    private Department department;

    private DepartmentTestFactory departmentTestFactory = new DepartmentTestFactory();

    Builder()
    {
      this.emailAddress = generateRandomEmail();
      this.department = departmentTestFactory.createDefault();
      this.birthday = java.sql.Date.valueOf(generateRandomDate());
      this.fullName = new Employee.FullName();
      this.fullName.setLastName(RandomStringUtils.randomAlphabetic(12));
      this.fullName.setFirstName(RandomStringUtils.randomAlphabetic(12));
    }

    public Builder emailAddress(String emailAddress)
    {
      this.emailAddress = emailAddress;
      return this;
    }

    public Builder fullName(Employee.FullName fullName)
    {
      this.fullName = fullName;
      return this;
    }

    public Builder birthday(Date birthday)
    {
      this.birthday = birthday;
      return this;
    }

    public Builder department(Department department)
    {
      this.department = department;
      return this;
    }

    public Employee create()
    {
      Employee employee = new Employee();
      return employee.setBirthday(birthday).setDepartment(department).setEmailAddress(emailAddress).setFullName(fullName);
    }
  }
}
