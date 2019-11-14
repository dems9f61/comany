package com.takeaway.employeeservice.department.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * User: StMinko Date: 18.03.2019 Time: 17:32
 *
 * <p>
 */
@Getter
@ToString
@EqualsAndHashCode
public class DepartmentResponse
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  @ApiModelProperty(example = "245")
  @NotNull
  @Min(1)
  private final Long id;

  @ApiModelProperty(example = "Human Resources (HR)")
  @NotBlank
  private final String departmentName;

  // ============================  Constructors  ===========================

  @JsonCreator
  public DepartmentResponse(@JsonProperty(value = "id") Long id, @JsonProperty(value = "departmentName") String departmentName)
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
