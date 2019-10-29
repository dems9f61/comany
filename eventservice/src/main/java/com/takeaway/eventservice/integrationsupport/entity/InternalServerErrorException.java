package com.takeaway.eventservice.integrationsupport.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko Date: 18.03.2019 Time: 17:19
 *
 * <p>
 */
public class InternalServerErrorException extends ApiException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public InternalServerErrorException(Throwable cause)
  {
    super(HttpStatus.INTERNAL_SERVER_ERROR, cause);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
