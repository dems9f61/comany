package com.takeaway.eventservice.messaging.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 19:11
 * <p/>
 */
@Component
public class EmployeeTestFactory
{

    public Employee createDefault()
    {
        return builder().create();
    }

    public List<Employee> createManyDefault(int count)
    {
        return manyBuilders(count).map(Builder::create)
                                  .collect(Collectors.toList());
    }

    private Stream<Builder> manyBuilders(int count)
    {
        return IntStream.range(0, count)
                        .mapToObj(i -> builder());
    }

    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder
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
            this.birthday = Date.from(generateRandomDate().atStartOfDay(ZoneId.systemDefault())
                                                          .toInstant());
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

        private LocalDate generateRandomDate()
        {
            long minDay = LocalDate.of(1970, 1, 1)
                                   .toEpochDay();
            long maxDay = LocalDate.now()
                                   .toEpochDay();
            long randomDay = ThreadLocalRandom.current()
                                              .nextLong(minDay, maxDay);

            return LocalDate.ofEpochDay(randomDay);
        }

        private String generateRandomEmail()
        {
            return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 24)) + "@" + (RandomStringUtils.randomAlphanumeric(10) + ".com");
        }

        public Employee create()
        {
            Employee employee = new Employee();
            return employee.setBirthday(birthday)
                           .setDepartment(department)
                           .setEmailAddress(emailAddress)
                           .setFullName(fullName);
        }
    }
}