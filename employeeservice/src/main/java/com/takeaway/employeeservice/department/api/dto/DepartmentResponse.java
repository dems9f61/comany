package com.takeaway.employeeservice.department.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private final long   id;

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
