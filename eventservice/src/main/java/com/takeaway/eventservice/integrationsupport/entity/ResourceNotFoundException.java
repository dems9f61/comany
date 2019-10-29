package com.takeaway.eventservice.integrationsupport.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:26
 *
 * <p>
 */
public class ResourceNotFoundException extends ApiException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public ResourceNotFoundException(String message)
  {
    super(HttpStatus.NOT_FOUND, message);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
