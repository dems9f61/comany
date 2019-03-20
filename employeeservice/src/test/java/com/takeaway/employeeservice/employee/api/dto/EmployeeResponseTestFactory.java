package com.takeaway.employeeservice.employee.api.dto;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 01:07
 * <p/>
 */
public class EmployeeResponseTestFactory extends AbstractTestFactory<EmployeeResponse, EmployeeResponseTestFactory.Builder>
{
    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<EmployeeResponse>
    {
        private String uuid;

        private String emailAddress;

        private String firstName;

        private String lastName;

        private Date birthday;

        private String departmentName;

        Builder()
        {
            this.uuid = UUID.randomUUID()
                            .toString();
            this.emailAddress = generateRandomEmail();
            this.firstName = RandomStringUtils.randomAlphabetic(20);
            this.lastName = RandomStringUtils.randomAlphabetic(20);
            this.birthday = Date.from(generateRandomDate().atStartOfDay(ZoneId.systemDefault())
                                                          .toInstant());
            this.departmentName = RandomStringUtils.randomAlphabetic(10);
        }

        public Builder uuid(String uuid)
        {
            this.uuid = uuid;
            return this;
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

        public EmployeeResponse create()
        {
            return new EmployeeResponse(uuid, emailAddress, firstName, lastName, birthday, departmentName);
        }
    }
}