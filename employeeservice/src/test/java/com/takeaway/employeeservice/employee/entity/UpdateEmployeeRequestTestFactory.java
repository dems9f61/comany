package com.takeaway.employeeservice.employee.entity;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 10.04.2019 Time: 23:00
 *
 * <p>
 */
@Component
public class UpdateEmployeeRequestTestFactory extends AbstractTestFactory<UpdateEmployeeRequest, UpdateEmployeeRequestTestFactory.Builder>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  public UpdateEmployeeRequestTestFactory.Builder builder()
  {
    return new UpdateEmployeeRequestTestFactory.Builder();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================

  public static class Builder implements AbstractTestFactory.Builder<UpdateEmployeeRequest>
  {
    private String emailAddress;

    private String firstName;

    private String lastName;

      private ZonedDateTime birthday;

    private String departmentName;

    Builder()
    {
      this.emailAddress = generateRandomEmail();
        this.birthday = createRandomBirthday();
      this.firstName = RandomStringUtils.randomAlphabetic(20);
      this.lastName = RandomStringUtils.randomAlphabetic(10);
      this.departmentName = RandomStringUtils.randomAlphabetic(10);
    }

    public UpdateEmployeeRequestTestFactory.Builder emailAddress(String emailAddress)
    {
      this.emailAddress = emailAddress;
      return this;
    }

    public UpdateEmployeeRequestTestFactory.Builder firstName(String firstName)
    {
      this.firstName = firstName;
      return this;
    }

    public UpdateEmployeeRequestTestFactory.Builder lastName(String lastName)
    {
      this.lastName = lastName;
      return this;
    }

      public UpdateEmployeeRequestTestFactory.Builder birthday(ZonedDateTime birthday)
    {
      this.birthday = birthday;
      return this;
    }

    public UpdateEmployeeRequestTestFactory.Builder departmentName(String departmentName)
    {
      this.departmentName = departmentName;
      return this;
    }

    public UpdateEmployeeRequest create()
    {
      return new UpdateEmployeeRequest(emailAddress, firstName, lastName, birthday, departmentName);
    }
  }

  // ============================  End of class  ===========================
}
