package com.takeaway.employeeservice.employee.api.dto;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:54
 * <p/>
 */
@Component
public class EmployeeRequestTestFactory extends AbstractTestFactory<EmployeeRequest, EmployeeRequestTestFactory.Builder>
{
    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<EmployeeRequest>
    {
        private String emailAddress;

        private String firstName;

        private String lastName;

        private Date birthday;

        private String departmentName;

        Builder()
        {
            this.emailAddress = generateRandomEmail();
            this.birthday = java.sql.Date.valueOf(generateRandomDate());
            this.firstName = RandomStringUtils.randomAlphabetic(20);
            this.lastName = RandomStringUtils.randomAlphabetic(10);
            this.departmentName = RandomStringUtils.randomAlphabetic(10);
        }

        public Builder emailAddress(String emailAddress)
        {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder firstName(String firstName)
        {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName)
        {
            this.lastName = lastName;
            return this;
        }

        public Builder birthday(Date birthday)
        {
            this.birthday = birthday;
            return this;
        }

        public Builder departmentName(String departmentName)
        {
            this.departmentName = departmentName;
            return this;
        }

        public EmployeeRequest create()
        {
            return new EmployeeRequest(emailAddress,
                                       firstName,
                                       lastName, birthday,
                                       departmentName);
        }
    }
}