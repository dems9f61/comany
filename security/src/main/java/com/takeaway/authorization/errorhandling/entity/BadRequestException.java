package com.takeaway.authorization.errorhandling.entity;

import org.springframework.http.HttpStatus;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:22
 *
 * <p>
 */
public class BadRequestException extends ApiException
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  public BadRequestException(Throwable cause)
  {
    super(cause, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message)
  {
    super(message, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, Throwable cause)
  {
    super(message, HttpStatus.BAD_REQUEST, cause);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
