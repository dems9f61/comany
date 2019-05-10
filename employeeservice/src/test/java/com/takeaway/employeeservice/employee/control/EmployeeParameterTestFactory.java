package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 11:11
 * <p/>
 */
@Component
public class EmployeeParameterTestFactory extends AbstractTestFactory<EmployeeParameter, EmployeeParameterTestFactory.Builder>
{
    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<EmployeeParameter>
    {
        private String emailAddress;

        private String firstName;

        private String lastName;

        private Date birthday;

        private String departmentName;

        Builder()
        {
            this.emailAddress = generateRandomEmail();
            this.firstName = RandomStringUtils.randomAlphabetic(10);
            this.lastName = RandomStringUtils.randomAlphabetic(13);
            this.birthday = java.sql.Date.valueOf(generateRandomDate());
            this.departmentName = RandomStringUtils.randomAlphabetic(24);
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

        public EmployeeParameter create()
        {
            return new EmployeeParameter(emailAddress, firstName, lastName, birthday, departmentName);
        }
    }
}