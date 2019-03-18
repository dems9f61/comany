package com.takeaway.employeeservice.department.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 14:51
 * <p/>
 */
@Component
public class DepartmentTestFactory
{
    public Department createDefault()
    {
        return builder().create();
    }

    public DepartmentTestFactoryBuilder builder()
    {
        return new DepartmentTestFactoryBuilder();
    }

    private Stream<DepartmentTestFactoryBuilder> manyBuilders(int count)
    {
        return IntStream.range(0, count)
                        .mapToObj(i -> builder());
    }

    public List<Department> createManyDefault(int count)
    {
        return manyBuilders(count).map(DepartmentTestFactoryBuilder::create)
                                  .collect(Collectors.toList());
    }

    public static class DepartmentTestFactoryBuilder
    {
        private long id;

        private String departmentName;

        DepartmentTestFactoryBuilder()
        {
            this.id = RandomUtils.nextInt(10, 10_000);
            this.departmentName = RandomStringUtils.randomAlphabetic(24);
        }

        public DepartmentTestFactoryBuilder id(long id)
        {
            this.id = id;
            return this;
        }

        public DepartmentTestFactoryBuilder departmentName(String departmentName)
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