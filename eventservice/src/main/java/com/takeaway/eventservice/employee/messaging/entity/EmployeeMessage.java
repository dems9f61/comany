package com.takeaway.eventservice.employee.messaging.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User: StMinko Date: 20.03.2019 Time: 15:59
 *
 * <p>
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMessage
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private EventType eventType;

  private Employee employee;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
