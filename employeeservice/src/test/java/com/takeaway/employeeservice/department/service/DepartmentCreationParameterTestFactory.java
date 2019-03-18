package com.takeaway.employeeservice.department.service;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 13:08
 * <p/>
 */
@Component
public class DepartmentCreationParameterTestFactory
        extends AbstractTestFactory<DepartmentCreationParameter, DepartmentCreationParameterTestFactory.Builder>
{

    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<DepartmentCreationParameter>
    {

        private String departmentName;

        private Builder()
        {
            this.departmentName = RandomStringUtils.randomAlphabetic(8);
        }

        public Builder departmentName(String departmentName)
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