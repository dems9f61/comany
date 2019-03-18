package com.takeaway.employeeservice.department.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 13:08
 * <p/>
 */
@Component
public class DepartmentCreationParameterTestFactory
{

    public DepartmentCreationParameterBuilder builder()
    {
        return new DepartmentCreationParameterBuilder();
    }

    public DepartmentCreationParameter createDefault()
    {
        return builder().create();
    }

    private Stream<DepartmentCreationParameterBuilder> manyBuilders(int count)
    {
        return IntStream.range(0, count)
                        .mapToObj(i -> builder());
    }

    public List<DepartmentCreationParameter> createManyDefault(int count)
    {
        return manyBuilders(count).map(DepartmentCreationParameterBuilder::create)
                                  .collect(Collectors.toList());
    }

    public static class DepartmentCreationParameterBuilder
    {

        private String departmentName;

        private DepartmentCreationParameterBuilder()
        {
            this.departmentName = RandomStringUtils.randomAlphabetic(8);
        }

        public DepartmentCreationParameterBuilder departmentName(String departmentName)
        {
            this.departmentName = departmentName;
            return this;
        }

        public DepartmentCreationParameter create()
        {
            return new DepartmentCreationParameter(departmentName);
        }
    }
}