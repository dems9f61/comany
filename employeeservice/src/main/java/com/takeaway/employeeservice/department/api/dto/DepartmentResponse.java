package com.takeaway.employeeservice.department.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:32
 * <p/>
 */
@Getter
@ToString
@EqualsAndHashCode
public class DepartmentResponse
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    @ApiModelProperty(example = "245")
    private final long   id;

    @ApiModelProperty(example = "Human Resources (HR)")
    private final String departmentName;

    // ============================  Constructors  ===========================

    public DepartmentResponse(@JsonProperty(value = "id", required = true) long id,
                              @JsonProperty(value = "departmentName", required = true) String departmentName)
    {
        this.id = id;
        this.departmentName = departmentName;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
