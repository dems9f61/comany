package com.takeaway.employeeservice.department.entity;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 18.03.2019 Time: 14:51
 *
 * <p>
 */
@Component
public class DepartmentTestFactory extends AbstractTestFactory<Department, DepartmentTestFactory.Builder>
{

  public Builder builder()
  {
    return new Builder();
  }

  public static class Builder implements AbstractTestFactory.Builder<Department>
  {
    private long id;

    private String departmentName;

    Builder()
    {
      this.id = RandomUtils.nextInt(10, 10_000);
      this.departmentName = RandomStringUtils.randomAlphabetic(24);
    }

    public Builder id(long id)
    {
      this.id = id;
      return this;
    }

    public Builder departmentName(String departmentName)
    {
      this.departmentName = departmentName;
      return this;
    }

    public Department create()
    {
      Department department = new Department();
      department.setId(id);
      department.setDepartmentName(departmentName);
      return department;
    }
  }
}
