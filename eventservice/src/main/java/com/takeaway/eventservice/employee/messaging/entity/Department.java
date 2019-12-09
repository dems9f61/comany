package com.takeaway.eventservice.employee.messaging.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * User: StMinko Date: 20.03.2019 Time: 14:09
 *
 * <p>
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department implements Serializable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private long id;

    private String departmentName;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
