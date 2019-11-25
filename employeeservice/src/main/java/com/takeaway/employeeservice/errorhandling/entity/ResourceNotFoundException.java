package com.takeaway.employeeservice.errorhandling.entity;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:26
 *
 * <p>
 */
public class ResourceNotFoundException extends RuntimeException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public ResourceNotFoundException(String message)
  {
    super(message);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
