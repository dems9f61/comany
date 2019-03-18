package com.takeaway.employeeservice.department.api.dto;

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
public class CreateDepartmentRequestTestFactory extends AbstractTestFactory<CreateDepartmentRequest, CreateDepartmentRequestTestFactory.Builder>
{

    public Builder builder()
    {
        return new Builder();
    }

    public static class Builder implements AbstractTestFactory.Builder<CreateDepartmentRequest>
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

        public CreateDepartmentRequest create()
        {
            return new CreateDepartmentRequest(departmentName);
        }
    }
}