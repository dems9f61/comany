package com.takeaway.eventservice.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 14:09
 * <p/>
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department
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
