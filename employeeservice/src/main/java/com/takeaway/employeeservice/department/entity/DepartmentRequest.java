package com.takeaway.employeeservice.department.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.takeaway.employeeservice.department.control.DepartmentParameter;
import com.takeaway.employeeservice.runtime.rest.DataView;
import com.takeaway.employeeservice.runtime.validation.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * User: StMinko Date: 18.03.2019 Time: 17:43
 *
 * <p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
public class DepartmentRequest
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @ApiModelProperty(example = "Human Resources (HR)")
    @NullOrNotBlank(groups = {DataView.PATCH.class})
    @NotBlank(message = "The department name must not be blank",
            groups = {DataView.POST.class, DataView.PUT.class})
    private final String departmentName;

    // ============================  Constructors  ===========================

    @JsonCreator
    public DepartmentRequest(@JsonProperty(value = "departmentName") String departmentName)
    {
        this.departmentName = departmentName;
    }

    public DepartmentParameter toDepartmentParameter()
    {
        return new DepartmentParameter(departmentName);
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
