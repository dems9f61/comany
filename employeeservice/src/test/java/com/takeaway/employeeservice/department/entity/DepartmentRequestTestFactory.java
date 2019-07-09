package com.takeaway.employeeservice.department.entity;

import com.takeaway.employeeservice.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 22:02
 * <p/>
 */
@Component
public class DepartmentRequestTestFactory extends AbstractTestFactory<DepartmentRequest, DepartmentRequestTestFactory.Builder>
{

    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<DepartmentRequest>
    {
        private String departmentName;

        Builder()
        {
            this.departmentName = RandomStringUtils.randomAlphabetic(23);
        }

        public Builder departmentName(String departmentName)
        {
            this.departmentName = departmentName;
            return this;
        }

        public DepartmentRequest create()
        {
            return new DepartmentRequest(departmentName);
        }
    }
}