package com.takeaway.employeeservice.department.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.takeaway.employeeservice.department.service.CreateDepartmentParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:43
 * <p/>
 */
@Getter
@ToString
@EqualsAndHashCode
public class CreateDepartmentRequest
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @ApiModelProperty(example = "Human Resources (HR)")
    @NotNull
    private final String departmentName;

    // ============================  Constructors  ===========================

    @JsonCreator
    CreateDepartmentRequest(@JsonProperty(value = "departmentName", required = true) String departmentName)
    {
        this.departmentName = departmentName;
    }

    public CreateDepartmentParameter toCreateDepartmentParameter()
    {
        return new CreateDepartmentParameter(departmentName);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
