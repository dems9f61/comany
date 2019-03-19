package com.takeaway.employeeservice.employee.service;

import com.takeaway.employeeservice.AbstractTestFactory;
import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 19:04
 * <p/>
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
            this.birthday = generateRandomDate();
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
            return employee.setBirthday(birthday)
                           .setDepartment(department)
                           .setEmailAddress(emailAddress)
                           .setFullName(fullName);
        }

        private Date generateRandomDate()
        {
            long minDay = LocalDate.of(1970, 1, 1)
                                   .toEpochDay();
            long maxDay = LocalDate.now()
                                   .toEpochDay();
            long randomDay = ThreadLocalRandom.current()
                                              .nextLong(minDay, maxDay);

            LocalDate localDate = LocalDate.ofEpochDay(randomDay);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                                      .toInstant());
        }

        private String generateRandomEmail()
        {
            return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 24)) + "@" + (RandomStringUtils.randomAlphanumeric(10) + ".com");
        }
    }
}