package com.takeaway.eventservice.employee.messaging.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * User: StMinko Date: 20.03.2019 Time: 14:09
 *
 * <p>
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee implements Serializable
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private UUID id;

    private String emailAddress;

    private FullName fullName = new FullName();

    private ZonedDateTime birthday;

    private Department department;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Data
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FullName implements Serializable
    {
        private String firstName;

        private String lastName;
    }

    // ============================  End of class  ===========================
}
